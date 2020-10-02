package amaralus.apps.hackandslash.graphics.entities.data;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

public class FloatVertexBufferObject extends VertexBufferObject<FloatBuffer> {

    public FloatVertexBufferObject(BufferType type, BufferUsage usage, float[] buffer) {
        super(type, usage, FloatBuffer.wrap(buffer));
    }

    public FloatVertexBufferObject(BufferType type, BufferUsage usage, FloatBuffer buffer) {
        super(type, usage, buffer);
    }

    @Override
    protected void initBuffer(FloatBuffer buffer) {
        glBufferData(type.getType(), buffer.array(), usage.getUsage());
    }

    @Override
    protected void resetBuffer(FloatBuffer buffer) {
        glBufferSubData(type.getType(), 0 , buffer.array());
    }
}
