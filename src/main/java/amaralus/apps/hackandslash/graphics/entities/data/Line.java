package amaralus.apps.hackandslash.graphics.entities.data;

import amaralus.apps.hackandslash.graphics.entities.Color;
import org.joml.Vector2f;

import java.nio.FloatBuffer;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

public class Line {

    private final VertexArraysObject vao;

    private Vector2f start;
    private Vector2f end;
    private Color color;

    public Line(Vector2f start, Vector2f end,Color color, VertexArraysObject vao) {
        this.start = start;
        this.end = end;
        this.vao = vao;
        this.color = color;
    }

    public void updateStart(Vector2f vector) {
        start = vector;
        var vbo = (FloatVertexBufferObject) vao.getBuffers().get(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(start)), 0);
    }

    public void updateEnd(Vector2f vector) {
        end = vector;
        var vbo = (FloatVertexBufferObject) vao.getBuffers().get(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(end)), vbo.getDataTypeBytes() * 2L);
    }

    public void updateColor(Color color) {
        this.color = color;
        var vbo = (FloatVertexBufferObject) vao.getBuffers().get(1);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(color.getVector())), 0);
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

    public Color getColor() {
        return color;
    }
}
