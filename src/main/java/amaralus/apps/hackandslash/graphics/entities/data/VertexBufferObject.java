package amaralus.apps.hackandslash.graphics.entities.data;

import amaralus.apps.hackandslash.common.Destroyable;

import java.nio.Buffer;

import static org.lwjgl.opengl.GL15.*;

public abstract class VertexBufferObject<B extends Buffer> implements Bindable, Destroyable {

    private final int id;
    protected final BufferType type;
    protected final BufferUsage usage;

    private VertexBufferObject(BufferType type, BufferUsage usage) {
        id = glGenBuffers();
        this.type = type;
        this.usage = usage;
    }

    public VertexBufferObject(BufferType type, BufferUsage usage, B buffer) {
        this(type, usage);
        bind();
        initBuffer(buffer);
        unbind();
    }

    public void updateBuffer(B buffer) {
        bind();
        resetBuffer(buffer);
        unbind();
    }

    protected abstract void initBuffer(B buffer);

    protected abstract void resetBuffer(B buffer);

    @Override
    public void destroy() {
        glDeleteBuffers(id);
    }

    @Override
    public void bind() {
        glBindBuffer(type.getType(), id);
    }

    @Override
    public void unbind() {
        glBindBuffer(type.getType(), 0);
    }

    @Override
    public int id() {
        return id;
    }

    public BufferType getType() {
        return type;
    }

    public BufferUsage getUsage() {
        return usage;
    }
}
