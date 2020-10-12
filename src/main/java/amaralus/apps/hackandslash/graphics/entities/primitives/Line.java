package amaralus.apps.hackandslash.graphics.entities.primitives;

import amaralus.apps.hackandslash.graphics.entities.Color;
import amaralus.apps.hackandslash.graphics.entities.gpu.VertexArraysObject;
import org.joml.Vector2f;

import java.nio.FloatBuffer;

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
        vbo.updateBuffer(FloatBuffer.wrap(toArray(start)), 0);
    }

    public void updateEnd(Vector2f vector) {
        end = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(end)), vbo.getDataTypeBytes() * 2L);
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }
}
