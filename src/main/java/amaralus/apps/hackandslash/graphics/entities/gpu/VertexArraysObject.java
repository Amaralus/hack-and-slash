package amaralus.apps.hackandslash.graphics.entities.gpu;

import amaralus.apps.hackandslash.graphics.entities.Bindable;
import amaralus.apps.hackandslash.resources.Resource;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexArraysObject extends Resource implements Bindable {

    private final int id;

    private List<VertexBufferObject> buffers;

    private VertexArraysObject(String resourceName) {
        super(resourceName);
        id = glGenVertexArrays();
    }

    public VertexArraysObject(String resourceName, List<VertexBufferObject> buffers) {
        this(resourceName);
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
        return id;
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
