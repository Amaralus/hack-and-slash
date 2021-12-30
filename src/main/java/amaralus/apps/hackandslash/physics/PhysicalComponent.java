package amaralus.apps.hackandslash.physics;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.common.Updatable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.graphics.scene.Node;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.physics.PhysicService.checkGlobalBorderCrossing;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.copy;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class PhysicalComponent implements Updatable, Destroyable {

    private Node entityNode;

    private Vector2f position = vec2();
    private Vector2f nodePosition = vec2();
    private Vector2f movementUnitVector = vec2();

    private Vector2f targetPosition;

    private float speedPerMs;

    public PhysicalComponent(Node entityNode) {
        this.entityNode = entityNode;
    }

    @Override
    public void destroy() {
        entityNode = null;
    }

    @Override
    public void update(long elapsedTime) {
        if (movementUnitVector.equals(0f, 0f))
            return;

        var distance = speedPerMs * elapsedTime;

        if (targetPosition == null) {
            nodePosition.add(movementUnitVector.mul(distance));
            movementUnitVector = vec2();
        } else
            moveToTarget(distance);

        // todo проверка границ нужно переделать на глобальную??
        nodePosition.sub(checkGlobalBorderCrossing(nodePosition));
        updatePosition();
    }

    private void updatePosition() {
        var parent = entityNode.getParent();
        if (parent instanceof Entity) {
            var parentPosition = ((Entity) parent).getPhysicalComponent().position;
            position = copy(parentPosition).add(nodePosition);
        } else
            position = nodePosition;
    }

    public void moveToTarget(float distance) {
        if (position.distanceSquared(targetPosition) < distance * distance) {
            position.set(targetPosition);
            targetPosition = null;
            movementUnitVector = vec2();
        } else {
            movementUnitVector = copy(targetPosition).sub(position).normalize(distance, copy(position));
            position.add(movementUnitVector);
        }
    }

    public void addMovementVector(Vector2f direction) {
        movementUnitVector.add(direction);
    }

    public void setTargetPosition(Vector2f targetPosition) {
        this.targetPosition = targetPosition;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getNodePosition() {
        return nodePosition;
    }

    public void setNodePosition(Vector2f nodePosition) {
        this.nodePosition = nodePosition;
    }

    public float getSpeed() {
        return speedPerMs * 1000;
    }

    public void setSpeed(float speed) {
        speedPerMs = speed * 0.001f;
    }
}
