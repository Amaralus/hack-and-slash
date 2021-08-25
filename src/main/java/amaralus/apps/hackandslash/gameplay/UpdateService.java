package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.TaskManager;
import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityService;
import amaralus.apps.hackandslash.gameplay.entity.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.*;

@Service
public class UpdateService implements Updateable {

    private static final Logger log = LoggerFactory.getLogger(UpdateService.class);

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
        asyncUpdate(elapsedTime);
    }

    public void asyncUpdate(long elapsedTime) {
        //sleep -> update
        var sleepToUpdateFuture = taskManager.executeTask(() -> filterByStatus(sleepingEntities, UPDATING))
                .thenAccept(toUpdate -> {
                    updatingEntities.addAll(toUpdate);
                    sleepingEntities.removeAll(toUpdate);
                });

        // getNewEntities
        entityService.activateNewEntities();
        sleepToUpdateFuture.join();
        processNewEntities();

        var updatedToRemoveFuture = taskManager.executeTasks(updatingEntities, entity -> updateEntity(entity, elapsedTime))
                .thenApply(updated -> filterByStatus(updated, REMOVE))
                .thenApply(toRemove -> {
                    updatingEntities.removeAll(toRemove);
                    return toRemove;
                });

        var sleepingToRemoveFuture = taskManager.executeTask(() -> filterByStatus(sleepingEntities, REMOVE))
                .thenApply(toRemove -> {
                    sleepingEntities.removeAll(toRemove);
                    return toRemove;
                });

        updatedToRemoveFuture.thenCombine(sleepingToRemoveFuture, (fromUpdate, fromSleep) -> {
            fromUpdate.addAll(fromSleep);
            entityService.removeEntities(fromUpdate);
            return updatingEntities;
        })
                .thenApply(updated -> filterByStatus(updated, SLEEPING))
                .thenAccept(toSleep -> {
                    sleepingEntities.addAll(toSleep);
                    updatingEntities.removeAll(toSleep);
                })
                .join();
    }

    private Entity updateEntity(Entity entity, long elapsedTime) {
        entity.update(elapsedTime);
        return entity;
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

    public void removeAll() {
        updatingEntities.clear();
        sleepingEntities.clear();
    }

    private List<Entity> filterByStatus(List<Entity> entityList, EntityStatus status) {
        return entityList.stream()
                .filter(entity -> entity.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void addEntities(List<Entity> entities) {
        newEntities.addAll(entities);
    }

    @Autowired
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }
}
