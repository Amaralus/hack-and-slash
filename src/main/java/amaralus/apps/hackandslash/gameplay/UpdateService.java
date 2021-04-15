package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UpdateService implements Updateable {

    private EntityService entityService;

    private final List<Entity> activeEntities = new ArrayList<>();

    @Override
    public void update(long elapsedTime) {
        entityService.activateNewEntities();

        activeEntities.forEach(entity -> entity.update(elapsedTime));
    }

    public void addEntity(Entity entity) {
        activeEntities.add(entity);
    }

    @Autowired
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }
}
