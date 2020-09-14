package amaralus.apps.hackandslash.graphics.data.sprites;

import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.VertexArraysObject;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class SimpleSprite implements Sprite {

    private final Texture texture;
    private final VertexArraysObject vao;
    private final Vector2f offsetToSpriteCenter = vec2(0.5f, 0.5f);

    public SimpleSprite(Texture texture, VertexArraysObject vao) {
        this.texture = texture;
        this.vao = vao;
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
    public Vector2f getOffsetToSpriteCenter() {
        return offsetToSpriteCenter;
    }

    @Override
    public void destroy() {
        vao.destroy();
    }
}
