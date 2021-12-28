package amaralus.apps.hackandslash.physics;

import amaralus.apps.hackandslash.common.Updatable;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.physics.PhysicService.checkGlobalBorderCrossing;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.copy;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class PhysicalComponent implements Updatable {

    private Vector2f position;
    private Vector2f movementDirection;

    private float speed;

    public PhysicalComponent(Vector2f position) {
        this.position = position;
        movementDirection = vec2();
    }

    @Override
    public void update(long elapsedTime) {
        if (!movementDirection.equals(0f, 0f)) {
            var speedCoef = speed * elapsedTime * 0.001f;

            position.add(movementDirection.mul(speedCoef));
            movementDirection = vec2();

            position.sub(checkGlobalBorderCrossing(position));
        }
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
