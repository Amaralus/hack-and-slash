package amaralus.apps.hackandslash.gameplay.entity;

import org.joml.Vector2f;

public class EntityLiveContext {

    private final Entity entity;

    EntityLiveContext(Entity entity) {
        this.entity = entity;
    }

    public EntityContext getEntityContext() {
        return entity.getEntityContext();
    }

    public long getEntityId() {
        return getEntityContext().getEntityId();
    }

    public EntityStatus getStatus() {
        return getEntityContext().getStatus();
    }

    public Vector2f getGlobalPosition() {
        return getEntityContext().getGlobalPosition();
    }

    public long getClientId() {
        return getEntityContext().getClientId();
    }
}
