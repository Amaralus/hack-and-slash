package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.Updateable;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class PhysicalComponent implements Updateable {

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
        }
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
