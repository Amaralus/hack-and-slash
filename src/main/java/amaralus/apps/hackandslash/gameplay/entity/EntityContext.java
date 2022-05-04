package amaralus.apps.hackandslash.gameplay.entity;

import lombok.Getter;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.copy;

@Getter
public class EntityContext {

    private final long entityId;
    private final EntityStatus status;
    private final Vector2f position;
    private final long clientId;

    protected EntityContext(Entity entity) {
        entityId = entity.getEntityId();
        status = entity.getStatus();
        position = copy(entity.getPhysicalComponent().getPosition());

        var client = entity.getMessageClient();
        clientId = client == null ? -1 : client.getId();
    }
}
