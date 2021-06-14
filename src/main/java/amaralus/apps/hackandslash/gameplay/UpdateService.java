package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.TaskManager;
import amaralus.apps.hackandslash.common.UnexpectedInterruptedException;
import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityService;
import amaralus.apps.hackandslash.gameplay.entity.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.*;

@Service
public class UpdateService implements Updateable {

    private static final Logger log = LoggerFactory.getLogger(UpdateService.class);

    private final TaskManager taskManager;
    private EntityService entityService;

    private final List<Entity> updatingEntities = new ArrayList<>(1000);
    private final List<Entity> sleepingEntities = new ArrayList<>(1000);

    public UpdateService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void update(long elapsedTime) {
        moveUpdatingEntities();

        entityService.activateNewEntities();

        updateEntities(elapsedTime);

        removeEntities();

        moveSleepingEntities();
    }

    private void updateEntities(long elapsedTime) {
        taskManager.addTasks(updatingEntities.stream()
                .map(entity -> toCallable(entity, elapsedTime))
                .collect(Collectors.toList()))
                .forEach(future -> {
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new UnexpectedInterruptedException(e);
                    }
                });
    }

    private Callable<?> toCallable(Entity entity, long elapsedTime) {
        return () -> {
            entity.update(elapsedTime);
            return null;
        };
    }

    public void addEntities(List<Entity> entities) {
        var toUpdate = new ArrayList<Entity>();
        var toSleep = new ArrayList<Entity>();

        for (var entity : entities)
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
    }

    public void removeAll() {
        updatingEntities.clear();
        sleepingEntities.clear();
    }

    private void moveUpdatingEntities() {
        var toUpdate = filterByStatus(sleepingEntities, UPDATING);
        updatingEntities.addAll(toUpdate);
        sleepingEntities.removeAll(toUpdate);
    }

    private void moveSleepingEntities() {
        var toSleep = filterByStatus(updatingEntities, SLEEPING);
        sleepingEntities.addAll(toSleep);
        updatingEntities.removeAll(toSleep);
    }

    private void removeEntities() {
        var fromUpdate = filterByStatus(updatingEntities, REMOVE);
        updatingEntities.removeAll(fromUpdate);

        var toRemove = new ArrayList<>(fromUpdate);

        var fromSleep = filterByStatus(sleepingEntities, REMOVE);
        sleepingEntities.removeAll(fromSleep);
        toRemove.addAll(fromSleep);

        entityService.removeEntities(toRemove);
    }

    private List<Entity> filterByStatus(List<Entity> entityList, EntityStatus status) {
        return entityList.stream()
                .filter(entity ->  entity.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Autowired
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }
}
