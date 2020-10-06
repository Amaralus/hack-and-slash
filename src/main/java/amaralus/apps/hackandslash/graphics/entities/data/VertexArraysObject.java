package amaralus.apps.hackandslash.graphics.entities.data;

import amaralus.apps.hackandslash.common.Destroyable;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexArraysObject implements Bindable, Destroyable {

    private final int id;

    private List<VertexBufferObject> buffers;
    private List<VertexBufferDataFormat> dataFormats;

    private VertexArraysObject() {
        id = glGenVertexArrays();
    }

    public VertexArraysObject(List<VertexBufferObject> buffers, List<VertexBufferDataFormat> dataFormats) {
        this();
        this.buffers = buffers;
        this.dataFormats = dataFormats;
        init();
    }

    private void init() {
        bind();
        buffers.forEach(Bindable::bind);
        dataFormats.forEach(VertexBufferDataFormat::enable);

        unbind();
    }

    @Override
    public void bind() {
        glBindVertexArray(id);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void destroy() {
        buffers = null;
        glDeleteVertexArrays(id);
    }

    public List<VertexBufferObject> getBuffers() {
        return buffers;
    }

    public static class VertexBufferDataFormat {

        private final int index;
        private final int size;
        private final long offset;
        private final int type;
        private final int stride;

        public VertexBufferDataFormat(int index, int size, int stride, long offset, Class<? extends Number> dataType) {
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
