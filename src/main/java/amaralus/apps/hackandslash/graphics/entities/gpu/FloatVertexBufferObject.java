package amaralus.apps.hackandslash.graphics.entities.gpu;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

public class FloatVertexBufferObject extends VertexBufferObject<FloatBuffer> {

    public FloatVertexBufferObject(String resourceName, BufferType type, BufferUsage usage, float[] buffer) {
        this(resourceName, type, usage, FloatBuffer.wrap(buffer));
    }

    public FloatVertexBufferObject(String resourceName, BufferType type, BufferUsage usage, FloatBuffer buffer) {
        super(resourceName, type, usage, buffer, Float.BYTES);
    }

    @Override
    protected void initBuffer(FloatBuffer buffer) {
        glBufferData(type.getType(), buffer.array(), usage.getUsage());
    }

    @Override
    protected void updateBufferData(FloatBuffer buffer, long offset) {
        glBufferSubData(type.getType(), offset , buffer.array());
    }
}
