package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.Updateable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateService implements Updateable {

    private final List<Entity> allEntities = new ArrayList<>();

    @Override
    public void update(long elapsedTime) {
        allEntities.forEach(entity -> entity.update(elapsedTime));
    }

    public void registerEntity(Entity... entities) {
        allEntities.addAll(Arrays.asList(entities));
    }

    public void removeEntity(Entity entity) {
        allEntities.remove(entity);
    }
}
