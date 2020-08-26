package amaralus.apps.hackandslash.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.VectMatrUtil.mat4;
import static amaralus.apps.hackandslash.VectMatrUtil.vec3;
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
    private final int vbo;
    private final int ebo;

    private final Shader textureShader;

    private final Matrix4f projection;

    private final int[] indices = {0, 1, 3, 1, 2, 3};

    public SpriteRenderer(float width, float height) {
        textureShader = new Shader("vertex", "fragment");

        projection = mat4().ortho(0.0f, width, height, 0.0f, -1.0f, 1.0f);

        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();
        setUpVertexData();
    }

    public void draw(Texture texture, Vector2f entityPos, Vector2f entitySize, float rotateAngle) {
        textureShader.use();

        var model = mat4()
                .translate(vec3(entityPos, 1f))
                .translate(vec3(0.5f * entitySize.x, 0.5f * entitySize.y, 0f))
                .rotate(toRadians(rotateAngle), vec3(0f, 0f, 1f))
                .translate(vec3(-0.5f * entitySize.x, -0.5f * entitySize.y, 0f))
                .scale(vec3(entitySize, 1f));

        textureShader.setMatrix4("model", model);
        textureShader.setMatrix4("projection", projection);

        texture.bind();

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }

    private void setUpVertexData() {
        float[] vertices = {
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f
        };

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
