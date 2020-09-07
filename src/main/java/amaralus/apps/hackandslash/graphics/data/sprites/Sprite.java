package amaralus.apps.hackandslash.graphics.data.sprites;

import amaralus.apps.hackandslash.Destroyable;
import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.VertexArraysObject;
import org.joml.Vector2f;

public interface Sprite extends Destroyable {

    Texture getTexture();

    Vector2f getTextureOffset();

    VertexArraysObject getVao();

    float getWidth();

    float getHeight();

    Vector2f getOffsetToSpriteCenter();
}
