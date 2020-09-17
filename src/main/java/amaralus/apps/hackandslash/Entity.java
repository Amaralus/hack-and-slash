package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.graphics.RenderComponent;
import org.joml.Vector2f;

public class Entity {

    private final RenderComponent renderComponent;
    private final Vector2f position;

    public Entity(RenderComponent renderComponent, Vector2f position) {
        this.renderComponent = renderComponent;
        this.position = position;
    }

    public void update(long elapsedTime) {
        renderComponent.update(elapsedTime);
    }

    public void moveLeft(float distance) {
        position.x -= distance;
    }

    public void moveRight(float distance) {
        position.x += distance;
    }

    public void moveUp(float distance) {
        position.y -= distance;
    }

    public void moveDown(float distance) {
        position.y += distance;
    }

    public RenderComponent getRenderComponent() {
        return renderComponent;
    }

    public Vector2f getPosition() {
        return position;
    }
}
