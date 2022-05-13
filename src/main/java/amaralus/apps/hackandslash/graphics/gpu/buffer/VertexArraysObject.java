package amaralus.apps.hackandslash.graphics.gpu.buffer;

import amaralus.apps.hackandslash.graphics.gpu.Bindable;
import amaralus.apps.hackandslash.resources.Resource;
import lombok.Getter;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexArraysObject implements Bindable, Resource<String> {

    private final int id;
    @Getter
    private final String resourceId;

    private List<VertexBufferObject> buffers;

    private VertexArraysObject(String resourceId) {
        this.resourceId = resourceId;
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
