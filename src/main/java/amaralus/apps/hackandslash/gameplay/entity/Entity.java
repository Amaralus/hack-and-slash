package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.common.Updatable;
import amaralus.apps.hackandslash.common.message.QueueMessageClient;
import amaralus.apps.hackandslash.gameplay.state.StateSystem;
import amaralus.apps.hackandslash.graphics.rendering.RenderComponent;
import amaralus.apps.hackandslash.physics.PhysicalComponent;
import amaralus.apps.hackandslash.scene.Node;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.NEW;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;
import static amaralus.apps.hackandslash.scene.NodeRemovingStrategy.CASCADE;

public class Entity extends Node implements Updatable {

    private static final AtomicLong entityIdSource = new AtomicLong();

    private final long entityId;
    private final PhysicalComponent physicalComponent;
    private RenderComponent renderComponent;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private EntityContext entityContext;

    private QueueMessageClient messageClient;
    private StateSystem stateSystem;

    private EntityStatus status;

    public Entity() {
        entityId = entityIdSource.incrementAndGet();
        status = NEW;
        physicalComponent = new PhysicalComponent(this);
        entityContext = new EntityContext(this);
    }

    @Override
    public void update(long elapsedTime) {
        if (stateSystem != null)
            stateSystem.update(elapsedTime);

        renderComponent.update(elapsedTime);
        updateContext();
    }

    private void updateContext() {
        var tmpContext = createContext();
        try {
            lock.writeLock().lock();
            entityContext = tmpContext;
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected EntityContext createContext() {
        return new EntityContext(this);
    }

    public EntityContext getEntityContext() {
        try {
            lock.readLock().lock();
            return entityContext;
        } finally {
            lock.readLock().unlock();
        }
    }

    public EntityLiveContext getLiveContext() {
        return new EntityLiveContext(this);
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

    public void setRenderComponent(RenderComponent renderComponent) {
        this.renderComponent = renderComponent;
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
