package amaralus.apps.hackandslash.graphics.entities.data;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

public class IntVertexBufferObject extends VertexBufferObject<IntBuffer> {

    public IntVertexBufferObject(BufferType type, BufferUsage usage, int[] buffer) {
        super(type, usage, IntBuffer.wrap(buffer));
    }

    public IntVertexBufferObject(BufferType type, BufferUsage usage, IntBuffer buffer) {
        super(type, usage, buffer);
    }

    @Override
    protected void initBuffer(IntBuffer buffer) {
        glBufferData(type.getType(), buffer.array(), usage.getUsage());
    }

    @Override
    protected void resetBuffer(IntBuffer buffer) {
        glBufferSubData(type.getType(), 0 , buffer.array());
    }
}
