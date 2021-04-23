package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityService;
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

    private EntityService entityService;

    private final List<Entity> updatingEntities = new ArrayList<>(1000);
    private final List<Entity> sleepingEntities = new ArrayList<>(1000);

    @Override
    public void update(long elapsedTime) {
        moveUpdatingEntities();

        entityService.activateNewEntities();

        updatingEntities.forEach(entity -> entity.update(elapsedTime));

        removeEntities();

        moveSleepingEntities();
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
        var toUpdate = sleepingEntities.stream()
                .filter(entity -> entity.getStatus() == UPDATING)
                .collect(Collectors.toList());
        updatingEntities.addAll(toUpdate);
        sleepingEntities.removeAll(toUpdate);
    }

    private void moveSleepingEntities() {
        var toSleep = updatingEntities.stream()
                .filter(entity -> entity.getStatus() == SLEEPING)
                .collect(Collectors.toList());
        sleepingEntities.addAll(toSleep);
        updatingEntities.removeAll(toSleep);
    }

    private void removeEntities() {
        var fromUpdate = updatingEntities.stream()
                .filter(entity -> entity.getStatus() == REMOVE)
                .collect(Collectors.toList());
        updatingEntities.removeAll(fromUpdate);

        var toRemove = new ArrayList<>(fromUpdate);

        var fromSleep = sleepingEntities.stream()
                .filter(entity -> entity.getStatus() == REMOVE)
                .collect(Collectors.toList());
        sleepingEntities.removeAll(fromSleep);
        toRemove.addAll(fromSleep);

        entityService.removeEntities(toRemove);
    }

    @Autowired
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }
}
