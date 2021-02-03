package amaralus.apps.hackandslash.graphics.entities;

import amaralus.apps.hackandslash.graphics.scene.Node;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.*;

public class Camera extends Node {

    private final float width;
    private final float height;
    private float scale = 1f;

    private final Vector2f position;

    private Vector2f leftTopPosition;
    private Matrix4f projection;

    public Camera(float width, float height) {
        this(width, height, vec2(0f, 0f));
    }

    public Camera(float width, float height, Vector2f position) {
        this.width = width;
        this.height = height;
        this.position = position;
        leftTopPosition = calcLeftTopPosition();
        projection = calcProjection();
    }

    private Matrix4f calcProjection() {
        return mat4().ortho(0.0f, getScaledWidth(), getScaledHeight(), 0.0f, -1.0f, 1.0f);
    }

    private Vector2f calcLeftTopPosition() {
        return copy(position).sub(vec2(getScaledWidth() * 0.5f, getScaledHeight() * 0.5f));
    }

    public Vector2f getEntityCamPos(Vector2f entityPos, Vector2f spriteSizeOfCam, Vector2f offsetToSpriteCenter) {
        return copy(entityPos)
                .sub(copy(spriteSizeOfCam).mul(offsetToSpriteCenter))
                .sub(leftTopPosition);
    }

    public Vector2f getWordPosOfScreenPos(Vector2f screenPosition) {
        return copy(leftTopPosition).add(screenPosition.mul(scale));
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

    public float getScaledWidth() {
        return width * scale;
    }

    public float getScaledHeight() {
        return height * scale;
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
        if (scale < 0.05f) scale = 0.05f;
        this.scale = scale;
        leftTopPosition = calcLeftTopPosition();
        projection = calcProjection();
    }

    public void addScale(float scale) {
        setScale(this.scale + scale);
    }
}
