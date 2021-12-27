package amaralus.apps.hackandslash.gameplay.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector2f;

@AllArgsConstructor
@Getter
public class EntityContext {

    private final long entityId;
    private final EntityStatus status;
    private final Vector2f globalPosition;
    private final long clientId;
}
