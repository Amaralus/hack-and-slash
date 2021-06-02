package amaralus.apps.hackandslash.graphics.entities.gpu;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

public class IntVertexBufferObject extends VertexBufferObject<IntBuffer> {

    public IntVertexBufferObject(String resourceName, BufferType type, BufferUsage usage, int[] buffer) {
        this(resourceName, type, usage, IntBuffer.wrap(buffer));
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
