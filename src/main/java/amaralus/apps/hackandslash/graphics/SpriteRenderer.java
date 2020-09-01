package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.camera.OrthoCamera;
import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.data.VertexBufferObject;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.VectMatrUtil.*;
import static org.joml.Math.toRadians;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

public class SpriteRenderer {

    private final Shader textureShader;
    VertexArraysObject vao;

    private final int[] vertexIndices = {0, 1, 3, 1, 2, 3};

    public SpriteRenderer() {
        textureShader = new Shader("vertex", "fragment");
        setUpVertexData();
    }

    public void draw(OrthoCamera camera, Texture texture, Vector2f entityPos, float rotateAngle) {
        textureShader.use();
        var textureSize = vec2(texture.getWidth() * camera.getScale(), texture.getHeight() * camera.getScale());

        var model = mat4()
                .translate(vec3(camera.getEntityCamPos(entityPos, textureSize), 1f))
                .translate(vec3(0.5f * textureSize.x, 0.5f * textureSize.y, 0f))
                .rotate(toRadians(rotateAngle), vec3(0f, 0f, 1f))
                .translate(vec3(-0.5f * textureSize.x, -0.5f * textureSize.y, 0f))
                .scale(vec3(textureSize, 1f));


        textureShader.setVec2("offset", vec2(calcOffset(64, 16, 1), calcOffset(72, 24, 2)));
        textureShader.setMat4("model", model);
        textureShader.setMat4("projection", camera.getProjection());

        texture.bind();

        vao.bind();
        glDrawElements(GL_TRIANGLES, vertexIndices.length, GL_UNSIGNED_INT, 0);
        vao.unbind();
    }

    public void destroy() {
        vao.destroy();
    }

    private float calcOffset(float textureLength, float frameLength, float frameNum) {
        return (frameLength * (frameNum - 1)) / textureLength;
    }

    private void setUpVertexData() {
        float xPos = 16f / 64f;
        float yPos = 24f / 72f;

        float[] vertices = {0f, yPos, 0f, 0f, xPos, 0f, xPos, yPos};

        vao = new VertexArraysObject(
                new VertexBufferObject(GL_ARRAY_BUFFER, vertices),
                new VertexBufferObject(GL_ELEMENT_ARRAY_BUFFER, vertexIndices)
        );
    }
}
