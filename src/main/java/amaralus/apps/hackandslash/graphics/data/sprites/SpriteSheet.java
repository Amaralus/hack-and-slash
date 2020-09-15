package amaralus.apps.hackandslash.graphics.data.sprites;

import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.VertexArraysObject;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class SpriteSheet extends SimpleSprite {

    private final float frameWidth;
    private final float frameHeight;
    private final Vector2f offsetToSpriteCenter;

    private final List<SpriteAnimation> spriteAnimations;
    private int activeSprite = 0;

    public SpriteSheet(Texture texture, VertexArraysObject vao, SpriteSheetData spriteSheetData) {
        super(texture, vao);

        frameWidth = spriteSheetData.getFrameWidth();
        frameHeight = spriteSheetData.getFrameHeight();

        spriteAnimations = new ArrayList<>(spriteSheetData.getFramesCount().size());
        for (int i = 0; i < spriteSheetData.getFramesCount().size(); i++)
            spriteAnimations.add(new SpriteAnimation(texture, spriteSheetData, i));

        offsetToSpriteCenter = frameTexturePosition(texture, spriteSheetData).mul(0.5f);
    }

    public static Vector2f frameTexturePosition(Texture texture, SpriteSheetData spriteSheetData) {
        return vec2(
                (float) spriteSheetData.getFrameWidth() / (float) texture.getWidth(),
                (float) spriteSheetData.getFrameHeight() / (float) texture.getHeight());
    }

    @Override
    public Vector2f getTextureOffset() {
        return getActiveSprite().getCurrentFrame().getFrameOffset();
    }

    @Override
    public Vector2f getOffsetToSpriteCenter() {
        return offsetToSpriteCenter;
    }

    public float getFrameWidth() {
        return frameWidth;
    }

    public float getFrameHeight() {
        return frameHeight;
    }

    public SpriteAnimation getActiveSprite() {
        return spriteAnimations.get(activeSprite);
    }

    public void setActiveSprite(int activeSprite) {
        this.activeSprite = activeSprite;
        getActiveSprite().reset();
    }
}
