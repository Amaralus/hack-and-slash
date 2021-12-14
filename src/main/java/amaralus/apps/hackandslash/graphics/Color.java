package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.utils.VectMatrUtil;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {

    public static final Color WHITE = rgb(1f, 1f, 1f);
    public static final Color BLACK = rgb(0f, 0f, 0f);
    public static final Color RED = rgb(1f, 0f, 0f);
    public static final Color GREEN = rgb(0f, 1f, 0f);
    public static final Color BLUE = rgb(0f, 0f, 1f);
    public static final Color YELLOW = rgb(1f, 1f, 0f);
    public static final Color MAGENTA = rgb(1f, 0f, 1f);
    public static final Color CYAN = rgb(0f, 1f, 1f);

    private final Vector4f vector;

    private Color(float r, float g, float b, float a) {
        vector = VectMatrUtil.vec4(r, g, b, a);
    }

    public static Color rgba(float r, float g, float b, float a) {
        return new Color(r, g, b, a);
    }

    public static Color rgba(int r, int g, int b, int a) {
        return rgba(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color rgb(float r, float g, float b) {
        return new Color(r, g, b, 1f);
    }

    public static Color rgba(int r, int g, int b) {
        return rgba(r / 255f, g / 255f, b / 255f, 1f);
    }

    public float r() {
        return vector.x;
    }

    public float g() {
        return vector.y;
    }

    public float b() {
        return vector.z;
    }

    public float a() {
        return vector.w;
    }

    public Vector4f rgba() {
        return vector;
    }

    public Vector3f rgb() {
        return VectMatrUtil.vec3(r(), g(), b());
    }
}
