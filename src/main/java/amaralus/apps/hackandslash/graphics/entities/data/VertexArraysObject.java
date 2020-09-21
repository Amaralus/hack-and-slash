package amaralus.apps.hackandslash.graphics.entities.data;

import amaralus.apps.hackandslash.common.Destroyable;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexArraysObject implements Bindable, Destroyable {

    private final int id;

    private List<VertexBufferObject> buffers;

    public VertexArraysObject() {
        id = glGenVertexArrays();
    }

    public VertexArraysObject(VertexBufferObject... buffers) {
        this();
        this.buffers = List.of(buffers);
        init();
    }

    private void init() {
        bind();
        buffers.forEach(Bindable::bind);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

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
}
