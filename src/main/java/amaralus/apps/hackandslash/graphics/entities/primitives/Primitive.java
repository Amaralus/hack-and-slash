package amaralus.apps.hackandslash.graphics.entities.primitives;

import amaralus.apps.hackandslash.graphics.entities.Color;
import amaralus.apps.hackandslash.graphics.entities.gpu.FloatVertexBufferObject;
import amaralus.apps.hackandslash.graphics.entities.gpu.VertexArraysObject;

import java.nio.FloatBuffer;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

public abstract class Primitive {

    private final VertexArraysObject vao;
    protected Color color;

    protected Primitive(VertexArraysObject vao, Color color) {
        this.vao = vao;
        this.color = color;
    }

    public void updateColor(Color color) {
        this.color = color;
        var vbo = getVbo(1);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(color.getVector())), 0);
    }

    protected FloatVertexBufferObject getVbo(int index) {
        return (FloatVertexBufferObject) vao.getBuffers().get(index);
    }

    public VertexArraysObject getVao() {
        return vao;
    }

    public Color getColor() {
        return color;
    }
}
