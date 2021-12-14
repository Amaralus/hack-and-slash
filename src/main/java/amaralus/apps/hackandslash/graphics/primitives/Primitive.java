package amaralus.apps.hackandslash.graphics.primitives;

import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.gpu.buffer.FloatVertexBufferObject;
import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.rendering.RenderComponent;

import static amaralus.apps.hackandslash.graphics.rendering.RenderComponent.RenderComponentType.PRIMITIVE;
import static amaralus.apps.hackandslash.utils.BufferUtil.bufferOf;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

public abstract class Primitive implements RenderComponent {

    private final VertexArraysObject vao;
    protected Color color;

    protected Primitive(VertexArraysObject vao, Color color) {
        this.vao = vao;
        this.color = color;
    }

    public void updateColor(Color color) {
        this.color = color;
        var vbo = getVbo(1);
        vbo.updateBuffer(bufferOf(toArray(color.rgba())));
    }

    @Override
    public void update(long elapsedTime) {
        // Примитивам пока не нужно обновляться
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

    @Override
    public RenderComponentType getRenderComponentType() {
        return PRIMITIVE;
    }
}
