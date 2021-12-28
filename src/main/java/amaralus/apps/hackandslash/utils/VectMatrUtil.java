package amaralus.apps.hackandslash.utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class VectMatrUtil {

    private VectMatrUtil() {
    }

    public static Vector2f vec2() {
        return new Vector2f();
    }

    public static Vector2f vec2(float x, float y) {
        return new Vector2f(x, y);
    }

    public static Vector3f vec3() {
        return new Vector3f();
    }

    public static Vector3f vec3(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

    public static Vector3f vec3(Vector2f vec2, float z) {
        return new Vector3f(vec2, z);
    }

    public static Vector4f vec4() {
        return new Vector4f();
    }

    public static Vector4f vec4(float x, float y, float z, float w) {
        return new Vector4f(x, y, z, w);
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

    public static String toStr(Vector2f vector) {
        return "[" + vector.x + "," + vector.y + "]";
    }

    public static String toStr(Vector3f vector) {
        return "[" + vector.x + "," + vector.y + "," + vector.z + "]";
    }

    public static Vector2f average(Vector2f... vectors) {
        var average = vec2();
        for (var vector : vectors)
            average.add(vector);
        return average.div(vectors.length);
    }

    public static Vector3f average(Vector3f... vectors) {
        var average = vec3();
        for (var vector : vectors)
            average.add(vector);
        return average.div(vectors.length);
    }

    public static float[] toArray(Vector2f... vectors) {
        float[] array = new float[vectors.length * 2];
        for (int i = 0, j = 0; i < vectors.length; i++, j = i * 2) {
            array[j] = vectors[i].x;
            array[j + 1] = vectors[i].y;
        }
        return array;
    }

    public static float[] toArray(Vector3f... vectors) {
        float[] array = new float[vectors.length * 3];
        for (int i = 0, j = 0; i < vectors.length; i++, j = i * 3) {
            array[j] = vectors[i].x;
            array[j + 1] = vectors[i].y;
            array[j + 2] = vectors[i].z;
        }
        return array;
    }

    public static float[] toArray(Vector4f... vectors) {
        float[] array = new float[vectors.length * 4];
        for (int i = 0, j = 0; i < vectors.length; i++, j = i * 4) {
            array[j] = vectors[i].x;
            array[j + 1] = vectors[i].y;
            array[j + 2] = vectors[i].z;
            array[j + 3] = vectors[i].w;
        }
        return array;
    }
}
