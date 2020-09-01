package amaralus.apps.hackandslash.graphics.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.VectMatrUtil.*;

public class OrthoCamera {

    private final float width;
    private final float height;
    private float scale = 1f;

    private final Vector2f position;
    private final Vector2f leftTopPosition;

    private final Matrix4f projection;

    public OrthoCamera(float width, float height, Vector2f position) {
        this.width = width;
        this.height = height;
        this.position = position;
        leftTopPosition = copy(position).sub(vec2(width * 0.5f, height * 0.5f));
        projection = mat4().ortho(0.0f, width, height, 0.0f, -1.0f, 1.0f);
    }

    public Vector2f getEntityCamPos(Vector2f entityPos, Vector2f textureSize) {
        return copy(entityPos)
                .sub(copy(textureSize).mul(0.5f))
                .sub(leftTopPosition);
    }

    public void moveLeft(float distance) {
        position.x -= distance;
        leftTopPosition.x -= distance;
    }

    public void moveRight(float distance) {
        position.x += distance;
        leftTopPosition.x += distance;
    }

    public void moveUp(float distance) {
        position.y -= distance;
        leftTopPosition.y -= distance;
    }

    public void moveDown(float distance) {
        position.y += distance;
        leftTopPosition.y += distance;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getLeftTopPosition() {
        return leftTopPosition;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
