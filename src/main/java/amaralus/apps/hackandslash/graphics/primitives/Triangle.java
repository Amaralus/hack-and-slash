package amaralus.apps.hackandslash.graphics.primitives;

import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.BufferUtil.bufferOf;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

public class Triangle extends Primitive {

    private Vector2f topPoint;
    private Vector2f bottomRightPoint;
    private Vector2f bottomLeftPoint;

    public Triangle(VertexArraysObject vao, Color color, Vector2f topPoint, Vector2f bottomRightPoint, Vector2f bottomLeftPoint) {
        super(vao, color);
        this.topPoint = topPoint;
        this.bottomRightPoint = bottomRightPoint;
        this.bottomLeftPoint = bottomLeftPoint;
    }

    public void updateTopPoint(Vector2f vector) {
        topPoint = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(bufferOf(toArray(topPoint)));
    }

    public void updateBottomRightPoint(Vector2f vector) {
        bottomRightPoint = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(bufferOf(toArray(bottomRightPoint)), vbo.getDataTypeBytes() * 2L);
    }

    public void updateBottomLeft(Vector2f vector) {
        bottomLeftPoint = vector;
        var vbo = getVbo(0);
        vbo.updateBuffer(bufferOf(toArray(bottomLeftPoint)), vbo.getDataTypeBytes() * 4L);
    }

    public Vector2f getTopPoint() {
        return topPoint;
    }

    public Vector2f getBottomRightPoint() {
        return bottomRightPoint;
    }

    public Vector2f getBottomLeftPoint() {
        return bottomLeftPoint;
    }
}
