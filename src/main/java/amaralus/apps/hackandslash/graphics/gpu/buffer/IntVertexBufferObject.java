package amaralus.apps.hackandslash.graphics.gpu.buffer;

import java.nio.IntBuffer;

import static amaralus.apps.hackandslash.utils.BufferUtil.bufferOf;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

public class IntVertexBufferObject extends VertexBufferObject<IntBuffer> {

    public IntVertexBufferObject(String resourceName, BufferType type, BufferUsage usage, int[] ints) {
        this(resourceName, type, usage, bufferOf(ints));
    }

    public IntVertexBufferObject(String resourceName, BufferType type, BufferUsage usage, IntBuffer buffer) {
        super(resourceName, type, usage, buffer, Integer.BYTES);
    }

    @Override
    protected void initBuffer(IntBuffer buffer) {
        glBufferData(type.getType(), buffer.array(), usage.getUsage());
    }

    @Override
    protected void updateBufferData(IntBuffer buffer, long offset) {
        glBufferSubData(type.getType(), offset , buffer.array());
    }
}
