package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.gameplay.InputComponent;
import amaralus.apps.hackandslash.gameplay.PhysicalComponent;
import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import amaralus.apps.hackandslash.graphics.scene.Node;
import org.joml.Vector2f;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.NEW;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;
import static amaralus.apps.hackandslash.graphics.scene.NodeRemovingStrategy.CASCADE;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.copy;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class Entity extends Node implements Updateable {

    public static final AtomicLong entityIdSource = new AtomicLong();

    private final long entityId;
    private final InputComponent inputComponent;
    private final PhysicalComponent physicalComponent;
    private final RenderComponent renderComponent;
    private Vector2f globalPosition;

    private EntityStatus status;

    public Entity(RenderComponent renderComponent, Vector2f position) {
        entityId = entityIdSource.incrementAndGet();
        status = NEW;
        inputComponent = new InputComponent();
        physicalComponent = new PhysicalComponent(position);
        this.renderComponent = renderComponent;
        globalPosition = vec2();
    }

    @Override
    public void update(long elapsedTime) {

        inputComponent.executeCommands(this);
        physicalComponent.update(elapsedTime);

        updateGlobalPosition();

        renderComponent.update(elapsedTime);
    }

    private void updateGlobalPosition() {
        var parent = getParent();
        if (parent instanceof Entity)
            globalPosition = copy(((Entity) parent).globalPosition).add(physicalComponent.getPosition());
        else
            globalPosition = physicalComponent.getPosition();
    }

    public void move(Vector2f direction) {
        physicalComponent.move(direction);
    }

    public long getEntityId() {
        return entityId;
    }

    public InputComponent getInputComponent() {
        return inputComponent;
    }

    public PhysicalComponent getPhysicalComponent() {
        return physicalComponent;
    }

    public RenderComponent getRenderComponent() {
        return renderComponent;
    }

    public Vector2f getGlobalPosition() {
        return globalPosition;
    }

    public void setGlobalPosition(Vector2f globalPosition) {
        this.globalPosition = globalPosition;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
        if (status == REMOVE && nodeRemovingStrategy == CASCADE) {
            for (Node node : getChildren())
                ((Entity) node).setStatus(REMOVE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return entityId == entity.entityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }
}
