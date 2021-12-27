package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.common.Updatable;
import amaralus.apps.hackandslash.common.message.QueueMessageClient;
import amaralus.apps.hackandslash.gameplay.PhysicalComponent;
import amaralus.apps.hackandslash.gameplay.state.StateSystem;
import amaralus.apps.hackandslash.graphics.rendering.RenderComponent;
import amaralus.apps.hackandslash.graphics.scene.Node;
import org.joml.Vector2f;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.NEW;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;
import static amaralus.apps.hackandslash.graphics.scene.NodeRemovingStrategy.CASCADE;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.copy;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class Entity extends Node implements Updatable {

    private static final AtomicLong entityIdSource = new AtomicLong();

    private final long entityId;
    private final PhysicalComponent physicalComponent;
    private final RenderComponent renderComponent;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private EntityContext entityContext;

    private QueueMessageClient messageClient;
    private StateSystem stateSystem;
    private Vector2f globalPosition;

    private EntityStatus status;

    public Entity(RenderComponent renderComponent, Vector2f position) {
        entityId = entityIdSource.incrementAndGet();
        status = NEW;
        physicalComponent = new PhysicalComponent(position);
        this.renderComponent = renderComponent;
        globalPosition = vec2();
        entityContext = new EntityContext(entityId, status, vec2(), -1);
    }

    @Override
    public void update(long elapsedTime) {
        if (stateSystem != null)
            stateSystem.update(elapsedTime);

        physicalComponent.update(elapsedTime);

        updateGlobalPosition();

        renderComponent.update(elapsedTime);
        updateContext();
    }

    private void updateContext() {
        var tmpContext = new EntityContext(entityId, status, copy(globalPosition), messageClient.getId());
        try {
            lock.writeLock().lock();
            entityContext = tmpContext;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public EntityContext getEntityContext() {
        try {
            lock.readLock().lock();
            return entityContext;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void updateGlobalPosition() {
        var parent = getParent();
        if (parent instanceof Entity)
            globalPosition = copy(((Entity) parent).globalPosition).add(physicalComponent.getPosition());
        else
            globalPosition = physicalComponent.getPosition();
    }

    public long getEntityId() {
        return entityId;
    }

    public PhysicalComponent getPhysicalComponent() {
        return physicalComponent;
    }

    public RenderComponent getRenderComponent() {
        return renderComponent;
    }

    public QueueMessageClient getMessageClient() {
        return messageClient;
    }

    public void setMessageClient(QueueMessageClient messageClient) {
        this.messageClient = messageClient;
    }

    public StateSystem getStateSystem() {
        return stateSystem;
    }

    public void setStateSystem(StateSystem stateSystem) {
        this.stateSystem = stateSystem;
        stateSystem.setEntity(this);
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
