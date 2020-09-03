package amaralus.apps.hackandslash.graphics.data;

import amaralus.apps.hackandslash.Destroyable;
import org.joml.Vector2f;

public interface Sprite extends Destroyable {

    Texture getTexture();

    Vector2f getTextureOffset();

    VertexArraysObject getVao();

    float getWidth();

    float getHeight();
}
