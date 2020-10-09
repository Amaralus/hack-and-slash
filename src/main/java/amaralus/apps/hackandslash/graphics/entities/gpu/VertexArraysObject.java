package amaralus.apps.hackandslash.graphics.entities.gpu;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.graphics.entities.Bindable;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexArraysObject implements Bindable, Destroyable {

    private final int id;

    private List<VertexBufferObject> buffers;

    private VertexArraysObject() {
        id = glGenVertexArrays();
    }

    public VertexArraysObject(List<VertexBufferObject> buffers) {
        this();
        this.buffers = buffers;
        init();
    }

    private void init() {
        bind();
        buffers.forEach(VertexBufferObject::initialBind);
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
