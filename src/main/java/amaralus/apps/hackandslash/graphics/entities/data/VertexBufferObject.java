package amaralus.apps.hackandslash.graphics.entities.data;

import amaralus.apps.hackandslash.common.Destroyable;

import java.nio.Buffer;

import static org.lwjgl.opengl.GL15.*;

public abstract class VertexBufferObject<B extends Buffer> implements Bindable, Destroyable {

    private final int id;
    private final int size;
    private final int dataTypeBytes;

    private boolean needDataFormat = true;

    protected final BufferType type;
    protected final BufferUsage usage;

    private VertexBufferObject(BufferType type, BufferUsage usage, int size, int dataTypeBytes) {
        id = glGenBuffers();
        this.type = type;
        this.usage = usage;
        this.size = size;
        this.dataTypeBytes = dataTypeBytes;
    }

    protected VertexBufferObject(BufferType type, BufferUsage usage, B buffer, int dataTypeBytes) {
        this(type, usage, buffer.capacity(), dataTypeBytes);
        bind();
        initBuffer(buffer);
        unbind();
    }

    public void updateBuffer(B buffer, long offset) {
        bind();
        updateBufferData(buffer, offset);
        unbind();
    }

    protected abstract void initBuffer(B buffer);

    protected abstract void updateBufferData(B buffer, long offset);

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

    public int getSize() {
        return size;
    }

    public int getDataTypeBytes() {
        return dataTypeBytes;
    }

    public boolean isNeedDataFormat() {
        return needDataFormat;
    }

    public void setNeedDataFormat(boolean needDataFormat) {
        this.needDataFormat = needDataFormat;
    }
}
