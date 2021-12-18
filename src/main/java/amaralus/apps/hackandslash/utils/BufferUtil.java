package amaralus.apps.hackandslash.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class BufferUtil {

    private BufferUtil() {}

    public static FloatBuffer bufferOf(float... floats) {
        return FloatBuffer.wrap(floats);
    }

    public static IntBuffer bufferOf(int... ints) {
        return IntBuffer.wrap(ints);
    }

    public static ByteBuffer bufferOf(byte... bytes) {
        return ByteBuffer.wrap(bytes);
    }
}
