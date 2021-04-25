package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.Updateable;
import org.joml.Vector2f;

public class PhysicalComponent implements Updateable {

    private Vector2f position;

    private float speed;
    private float speedCoef;

    public PhysicalComponent(Vector2f position) {
        this.position = position;
    }

    @Override
    public void update(long elapsedTime) {
        speedCoef = speed * elapsedTime * 0.001f;
    }

    public void move(Vector2f direction) {
        position.add(direction.mul(speedCoef));
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
