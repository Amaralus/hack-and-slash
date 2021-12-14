package amaralus.apps.hackandslash.graphics.entities.gpu.buffer;

import amaralus.apps.hackandslash.graphics.entities.Bindable;
import amaralus.apps.hackandslash.resources.Resource;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public abstract class VertexBufferObject<B extends Buffer> extends Resource implements Bindable {

    private final int id;
    private final int size;
    private final int dataTypeBytes;

    protected final BufferType type;
    protected final BufferUsage usage;

    private List<DataFormat> dataFormats;

    private VertexBufferObject(String resourceName, BufferType type, BufferUsage usage, int size, int dataTypeBytes) {
        super(resourceName);
        id = glGenBuffers();
        dataFormats = new ArrayList<>();
        this.type = type;
        this.usage = usage;
        this.size = size;
        this.dataTypeBytes = dataTypeBytes;
    }

    protected VertexBufferObject(String resourceName, BufferType type, BufferUsage usage, B buffer, int dataTypeBytes) {
        this(resourceName, type, usage, buffer.capacity(), dataTypeBytes);
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

    void initialBind() {
        bind();
        for (var dataFormat : dataFormats) dataFormat.enable();
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

    public void setDataFormats(List<DataFormat> dataFormats) {
        this.dataFormats = dataFormats;
    }

    public static class DataFormat {

        private final int index;
        private final int size;
        private final long offset;
        private final int type;
        private final int stride;

        public DataFormat(int index, int size, int stride, long offset, Class<? extends Number> dataType) {
            this.index = index;
            this.size = size;
            if (dataType.equals(Float.TYPE)) {
                this.type = GL_FLOAT;
                this.stride = stride * Float.BYTES;
                this.offset = offset * Float.BYTES;
            } else
                throw new IllegalArgumentException("unexpected dataType: " + dataType);
        }

        private void enable() {
            glVertexAttribPointer(index, size, type, false, stride, offset);
            glEnableVertexAttribArray(index);
        }
    }
}
