package amaralus.apps.hackandslash.graphics;

import org.joml.Vector2f;

import static amaralus.apps.hackandslash.VectMatrUtil.*;
import static org.joml.Math.toRadians;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SpriteRenderer {

    private final int vao;
    private final int verticesVbo;
    private final int textureCoordsVbo;
    private final int ebo;

    private final Shader textureShader;

    private final int[] vertexIndices = {0, 1, 3, 1, 2, 3};

    public SpriteRenderer() {
        textureShader = new Shader("vertex", "fragment");

        vao = glGenVertexArrays();
        verticesVbo = glGenBuffers();
        textureCoordsVbo = glGenBuffers();
        ebo = glGenBuffers();
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

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, vertexIndices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(verticesVbo);
        glDeleteBuffers(ebo);
    }

    private float calcOffset(float textureLength, float frameLength, float frameNum) {
        return (frameLength * (frameNum - 1)) / textureLength;
    }

    private void setUpVertexData() {
        float xPos = 16f / 64f;
        float yPos = 24f / 72f;

        float[] vertices = {0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f,};
        float[] textureCoords = {0f, yPos, 0f, 0f, xPos, 0f, xPos, yPos};

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, verticesVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, textureCoordsVbo);
        glBufferData(GL_ARRAY_BUFFER, textureCoords, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, vertexIndices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * Float.BYTES, 0L);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
