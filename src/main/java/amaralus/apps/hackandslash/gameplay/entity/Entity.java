package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.gameplay.InputComponent;
import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import amaralus.apps.hackandslash.graphics.scene.Node;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.copy;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class Entity extends Node implements Updateable {

    private final InputComponent inputComponent;
    private final RenderComponent renderComponent;
    private Vector2f position;
    private Vector2f globalPosition;

    private float speedPerSec;
    private float speedCoef;

    public Entity(RenderComponent renderComponent, Vector2f position) {
        inputComponent = new InputComponent();
        this.renderComponent = renderComponent;
        this.position = position;
        globalPosition = vec2();
    }

    @Override
    public void update(long elapsedTime) {
        speedCoef = speedPerSec * elapsedTime * 0.001f;

        inputComponent.executeCommands(this);
        updateGlobalPosition();

        renderComponent.update(elapsedTime);
    }

    private void updateGlobalPosition() {
        var parent = getParent();
        if (parent instanceof Entity)
            globalPosition = copy(((Entity) parent).globalPosition).add(position);
        else
            globalPosition = position;
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

    public Vector2f getGlobalPosition() {
        return globalPosition;
    }

    public void setGlobalPosition(Vector2f globalPosition) {
        this.globalPosition = globalPosition;
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
