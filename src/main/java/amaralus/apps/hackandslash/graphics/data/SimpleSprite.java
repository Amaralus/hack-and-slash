package amaralus.apps.hackandslash.graphics.data;

import org.joml.Vector2f;

import static amaralus.apps.hackandslash.VectMatrUtil.vec2;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public class SimpleSprite implements Sprite {

    private final Texture texture;
    private final VertexArraysObject vao;

    public SimpleSprite(Texture texture) {
        this(texture, 1f, 1f);
    }

    public SimpleSprite(Texture texture, float xPos, float yPos) {
        this.texture = texture;

        float[] vertices = {0f, yPos, 0f, 0f, xPos, 0f, xPos, yPos};
        int[] vertexIndices = {0, 1, 3, 1, 2, 3};

        vao = new VertexArraysObject(
                new VertexBufferObject(GL_ARRAY_BUFFER, vertices),
                new VertexBufferObject(GL_ELEMENT_ARRAY_BUFFER, vertexIndices)
        );
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public VertexArraysObject getVao() {
        return vao;
    }

    @Override
    public Vector2f getTextureOffset()  {
        return vec2(0f, 0f);
    }

    @Override
    public float getWidth() {
        return texture.getWidth();
    }

    @Override
    public float getHeight() {
        return texture.getHeight();
    }

    @Override
    public void destroy() {
        vao.destroy();
    }
}
