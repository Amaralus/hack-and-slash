package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.Updatable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityService;
import amaralus.apps.hackandslash.gameplay.entity.EntityStatus;
import amaralus.apps.hackandslash.reactive.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.SLEEPING;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.UPDATING;

@Service
@Slf4j
public class UpdateService implements Updatable {

    private final TaskManager taskManager;
    private EntityService entityService;

    private final List<Entity> updatingEntities = new ArrayList<>(1000);
    private final List<Entity> newEntities = new ArrayList<>(100);
    private final List<Entity> sleepingEntities = new ArrayList<>(1000);

    public UpdateService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void update(long elapsedTime) {
        // синхронизируем sleep -> update
        var sleepToUpdateFuture = sleepToUpdate();

        // получение новых сущностей
        entityService.activateNewEntities();
        sleepToUpdateFuture.join();
        // синхронизация new -> sleep, update
        processNewEntities();

        // обновление сущностей
        taskManager.executeTasks(updatingEntities, entity -> updateEntity(entity, elapsedTime)).join();

        // в будущем тут будет фаза распространения изменений

        // удаление сущностей
        removeAfterUpdate().join();

        // синхронизация update -> sleep
        updateToSleep().join();
    }

    private Entity updateEntity(Entity entity, long elapsedTime) {
        entity.update(elapsedTime);
        return entity;
    }

    private CompletableFuture<Void> sleepToUpdate() {
        var sleepFiltered = taskManager.supplyAsync(() -> filterByStatus(sleepingEntities, UPDATING));

        return CompletableFuture.allOf(
                taskManager.acceptAsync(sleepFiltered, updatingEntities::addAll),
                taskManager.acceptAsync(sleepFiltered, sleepingEntities::removeAll));
    }

    public void processNewEntities() {
        var toUpdate = new ArrayList<Entity>();
        var toSleep = new ArrayList<Entity>();

        for (var entity : newEntities)
            switch (entity.getStatus()) {
                case NEW:
                    entity.setStatus(UPDATING);
                    log.warn("Непредвиденная миграция статуса NEW -> UPDATING для сущности id={}", entity.getEntityId());
                    toUpdate.add(entity);
                    break;
                case UPDATING:
                    toUpdate.add(entity);
                    break;
                case SLEEPING:
                    toSleep.add(entity);
                    break;
                default:
                    log.warn("Сущность id={} с непредвиденным статусом {} проигнорирована", entity.getEntityId(), entity.getStatus());
            }

        updatingEntities.addAll(toUpdate);
        sleepingEntities.addAll(toSleep);
        newEntities.clear();
    }

    public CompletableFuture<Void> removeAfterUpdate() {
        var updateFiltered = taskManager.supplyAsync(() -> filterByStatus(updatingEntities, REMOVE));
        var sleepFiltered = taskManager.supplyAsync(() -> filterByStatus(sleepingEntities, REMOVE));
        var updateRemove = taskManager.acceptAsync(updateFiltered, updatingEntities::removeAll);
        var sleepRemove = taskManager.acceptAsync(sleepFiltered, sleepingEntities::removeAll);

        var combine = updateFiltered.thenCombine(sleepFiltered, (fromUpdate, fromSleep) -> {
            fromUpdate.addAll(fromSleep);
            entityService.removeEntities(fromUpdate);
            return null;
        });

        return CompletableFuture.allOf(updateRemove, sleepRemove, combine);
    }

    private CompletableFuture<Void> updateToSleep() {
        var updateFiltered = taskManager.supplyAsync(() -> filterByStatus(updatingEntities, SLEEPING));

        return CompletableFuture.allOf(
                taskManager.acceptAsync(updateFiltered, sleepingEntities::addAll),
                taskManager.acceptAsync(updateFiltered, updatingEntities::removeAll)
        );
    }

    private List<Entity> filterByStatus(List<Entity> entityList, EntityStatus status) {
        return entityList.stream()
                .filter(entity -> entity.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void addEntities(List<Entity> entities) {
        newEntities.addAll(entities);
    }

    public void removeAll() {
        newEntities.clear();
        updatingEntities.clear();
        sleepingEntities.clear();
    }

    @Autowired
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }
}
