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
    private Vector2f movementDirection;

    private float speed;

    public PhysicalComponent(Node entityNode) {
        this.entityNode = entityNode;
        movementDirection = vec2();
    }

    @Override
    public void update(long elapsedTime) {
        if (movementDirection.equals(0f, 0f))
            return;

        var speedCoef = speed * elapsedTime * 0.001f;

        nodePosition.add(movementDirection.mul(speedCoef));
        movementDirection = vec2();

        // todo проверка границ нужно переделать на глобальную
        nodePosition.sub(checkGlobalBorderCrossing(nodePosition));

        updatePosition();
    }

    @Override
    public void destroy() {
        entityNode = null;
    }

    private void updatePosition() {
        var parent = entityNode.getParent();
        if (parent instanceof Entity) {
            var parentPosition = ((Entity) parent).getPhysicalComponent().position;
            position = copy(parentPosition).add(nodePosition);
        } else
            position = nodePosition;
    }

    public static void moveTo(Vector2f position, Vector2f to, float distance) {
        if (position.distanceSquared(to) < distance * distance)
            position.set(to);
        else
            position.add(copy(to).sub(position).normalize(distance, copy(position)));
    }

    public void move(Vector2f direction) {
        movementDirection.add(direction);
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
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
