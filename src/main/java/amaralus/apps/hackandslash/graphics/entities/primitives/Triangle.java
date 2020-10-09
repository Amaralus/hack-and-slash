package amaralus.apps.hackandslash.graphics.entities.primitives;

import amaralus.apps.hackandslash.graphics.entities.Color;
import amaralus.apps.hackandslash.graphics.entities.gpu.VertexArraysObject;
import org.joml.Vector2f;

import java.nio.FloatBuffer;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

public class Triangle extends Primitive {

    private Vector2f first;
    private Vector2f second;
    private Vector2f third;

    public Triangle(VertexArraysObject vao, Color color, Vector2f first, Vector2f second, Vector2f third) {
        super(vao, color);
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public void updateFirst(Vector2f vector) {
        first = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(first)), 0);
    }

    public void updateSecond(Vector2f vector) {
        second = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(second)), vbo.getDataTypeBytes() * 2L);
    }

    public void updateThird(Vector2f vector) {
        third = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(FloatBuffer.wrap(toArray(third)), vbo.getDataTypeBytes() * 4L);
    }

    public Vector2f getFirst() {
        return first;
    }

    public Vector2f getSecond() {
        return second;
    }

    public Vector2f getThird() {
        return third;
    }
}
