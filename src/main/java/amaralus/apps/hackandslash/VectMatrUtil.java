package amaralus.apps.hackandslash;

import org.joml.Vector2f;
import org.joml.Vector3f;

public final class VectMatrUtil {

    private VectMatrUtil() {}

    public static Vector3f copy(Vector3f vector) {
        return new Vector3f(vector);
    }

    public static Vector2f copy(Vector2f vector) {
        return new Vector2f(vector);
    }
}
