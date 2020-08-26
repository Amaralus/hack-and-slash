package amaralus.apps.hackandslash;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class VectMatrUtil {

    private VectMatrUtil() {}

    public static Vector2f vec2(float x, float y) {
        return new Vector2f(x, y);
    }

    public static Vector3f vec3(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

    public static Vector3f vec3(Vector2f vec2, float z) {
        return new Vector3f(vec2, z);
    }

    public static Matrix4f mat4() {
        return new Matrix4f();
    }

    public static Vector3f copy(Vector3f vector) {
        return new Vector3f(vector);
    }

    public static Vector2f copy(Vector2f vector) {
        return new Vector2f(vector);
    }
}
