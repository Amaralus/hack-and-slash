package amaralus.apps.hackandslash.graphics.entities.data;

import org.joml.Vector2f;

import java.nio.FloatBuffer;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

public class Line {

    private final Vector2f start;
    private final Vector2f end;
    private final VertexArraysObject vao;

    public Line(Vector2f start, Vector2f end, VertexArraysObject vao) {
        this.start = start;
        this.end = end;
        this.vao = vao;
    }

    public void updateStart(Vector2f vector) {
        var vbo = (FloatVertexBufferObject) vao.getBuffers().get(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(vector)), 0);
    }

    public void updateEnd(Vector2f vector) {
        var vbo = (FloatVertexBufferObject) vao.getBuffers().get(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(vector)), vbo.getDataTypeBytes() * 2L);
    }

    public VertexArraysObject getVao() {
        return vao;
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }
}
