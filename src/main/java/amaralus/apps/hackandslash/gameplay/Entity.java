package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.toStr;

public class Entity implements Updateable {

    private final InputComponent inputComponent;
    private final RenderComponent renderComponent;
    private Vector2f position;

    private float speedPerSec;
    private float speedCoef;

    public Entity(RenderComponent renderComponent, Vector2f position, float speedPerSec) {
        inputComponent = new InputComponent();
        this.renderComponent = renderComponent;
        this.position = position;
        this.speedPerSec = speedPerSec;
    }

    @Override
    public void update(long elapsedTime) {
        speedCoef = speedPerSec * elapsedTime * 0.001f;

        inputComponent.executeCommands(this);

        renderComponent.update(elapsedTime);
    }

    public void move(Vector2f direction) {
        position.add(direction.mul(speedCoef));
    }

    public InputComponent getInputComponent() {
        return inputComponent;
    }

    public RenderComponent getRenderComponent() {
        return renderComponent;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getSpeedPerSec() {
        return speedPerSec;
    }

    public void setSpeedPerSec(float speedPerSec) {
        this.speedPerSec = speedPerSec;
    }
}
