package amaralus.apps.hackandslash.graphics.data;

import amaralus.apps.hackandslash.Destroyable;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject implements Bindable, Destroyable {

    private final int id;
    private final int bufferType;

    private VertexBufferObject(int bufferType) {
        id = glGenBuffers();
        this.bufferType = bufferType;
    }

    public VertexBufferObject(int bufferType, float[] buffer) {
        this(bufferType);
        bind();
        glBufferData(bufferType, buffer, GL_STATIC_DRAW);
        unbind();
    }

    public VertexBufferObject(int bufferType, int[] buffer) {
        this(bufferType);
        bind();
        glBufferData(bufferType, buffer, GL_STATIC_DRAW);
        unbind();
    }

    @Override
    public void destroy() {
        glDeleteBuffers(id);
    }

    @Override
    public void bind() {
        glBindBuffer(bufferType, id);
    }

    @Override
    public void unbind() {
        glBindBuffer(bufferType, 0);
    }

    @Override
    public int id() {
        return id;
    }
}
