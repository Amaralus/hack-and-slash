package amaralus.apps.hackandslash.graphics.data;

import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.VectMatrUtil.vec2;

public class SpriteSheet extends SimpleSprite {

    private final float frameWidth;
    private final float frameHeight;
    private float currentXFrame = 1;
    private float currentYFrame = 1;

    public SpriteSheet(Texture texture, SpriteSheetData spriteSheetData) {
        super(
                texture,
                (float) spriteSheetData.getFrameWidth() / (float) texture.getWidth(),
                (float) spriteSheetData.getFrameHeight() / (float) texture.getHeight());
        frameWidth = spriteSheetData.getFrameWidth();
        frameHeight = spriteSheetData.getFrameHeight();
    }

    @Override
    public Vector2f getTextureOffset() {
        return vec2(
                calcOffset(getTexture().getWidth(), frameWidth, currentXFrame),
                calcOffset(getTexture().getHeight(), frameHeight, currentYFrame));
    }

    private float calcOffset(float textureLength, float frameLength, float frameNum) {
        return (frameLength * (frameNum - 1)) / textureLength;
    }

    public float getFrameWidth() {
        return frameWidth;
    }

    public float getFrameHeight() {
        return frameHeight;
    }

    public float getCurrentXFrame() {
        return currentXFrame;
    }

    public void setCurrentXFrame(float currentXFrame) {
        this.currentXFrame = currentXFrame;
    }

    public float getCurrentYFrame() {
        return currentYFrame;
    }

    public void setCurrentYFrame(float currentYFrame) {
        this.currentYFrame = currentYFrame;
    }
}
