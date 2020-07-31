package amaralus.apps.hackandslash;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.joml.Math.*;

public class Camera {

    private final float width;
    private final float height;

    private final Vector3f position;
    private final Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
    private Vector3f cameraFront = new Vector3f(0f, 0f, -1f);

    private float fov = 45f;

    private float yaw = -90.0f;
    private float pitch = 0.0f;

    public Camera(float width, float height, float x, float y, float z) {
        this(width, height, new Vector3f(x, y, z));
    }

    public Camera(float width, float height, Vector3f position) {
        this.width = width;
        this.height = height;
        this.position = position;
    }

    public void moveForward(float distance) {
        position.add(cameraFront().mul(distance));
    }

    public void moveBackward(float distance) {
        position.sub(cameraFront().mul(distance));
    }

    public void moveLeft(float distance) {
        position.sub(cameraFront().cross(cameraUp()).normalize().mul(distance));
    }

    public void moveRight(float distance) {
        position.add(cameraFront().cross(cameraUp()).normalize().mul(distance));
    }

    public void moveUp(float distance) {
        position.add(cameraUp().mul(distance));
    }

    public void moveDown(float distance) {
        position.sub(cameraUp().mul(distance));
    }

    public void rotate(float xAngle, float yAngle) {
        yaw += xAngle;
        pitch += yAngle;

        if (pitch > 89.0f) pitch = 89.0f;
        if (pitch < -89.0f) pitch = -89.0f;

        cameraFront = new Vector3f(
                cos(toRadians(pitch)) * cos(toRadians(yaw)),
                sin(toRadians(pitch)),
                cos(toRadians(pitch)) * sin(toRadians(yaw)))
                .normalize();
    }

    public Matrix4f viewMatrix() {
        return new Matrix4f()
                .perspective(toRadians(fov), width / height, 0.1f, 100.0f)
                .lookAt(position(), position().add(cameraFront()), cameraUp());
    }

    public FloatBuffer viewMatrixBuffer() {
        return viewMatrix().get(BufferUtils.createFloatBuffer(16));
    }

    public Vector3f position() {
        return new Vector3f(position);
    }

    public Vector3f cameraFront() {
        return new Vector3f(cameraFront);
    }

    public Vector3f cameraUp() {
        return new Vector3f(cameraUp);
    }

    public float fov() {
        return fov;
    }

    public void fov(float fov) {
        this.fov = fov;
    }
}
