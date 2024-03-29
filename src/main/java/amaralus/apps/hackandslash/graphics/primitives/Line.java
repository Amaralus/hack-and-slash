package amaralus.apps.hackandslash.graphics.primitives;

import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.BufferUtil.bufferOf;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

public class Line extends Primitive {
    
    private Vector2f start;
    private Vector2f end;

    public Line(VertexArraysObject vao, Color color, Vector2f start, Vector2f end) {
        super(vao, color);
        this.start = start;
        this.end = end;
    }

    public void updateStart(Vector2f vector) {
        start = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(bufferOf(toArray(start)));
    }

    public void updateEnd(Vector2f vector) {
        end = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(bufferOf(toArray(end)), vbo.getDataTypeBytes() * 2L);
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }
}
