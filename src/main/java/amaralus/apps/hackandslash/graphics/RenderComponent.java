package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.data.sprites.FramesStrip;
import amaralus.apps.hackandslash.graphics.data.sprites.Sprite;
import amaralus.apps.hackandslash.graphics.data.sprites.SpriteSheet;
import org.joml.Vector2f;

import java.util.List;
import java.util.stream.Collectors;

public class RenderComponent {

    private final SpriteSheet sprite;
    private final List<FrameStripAnimation> animations;

    private int currentFrameStrip;
    private float spriteRotateAngle;

    public RenderComponent(SpriteSheet sprite) {
        this.sprite = sprite;
        animations = sprite.getFramesStrips().stream()
                .map(framesStrip -> new FrameStripAnimation(1000L, framesStrip.getFramesCount()))
                .collect(Collectors.toList());
    }

    public void update(long elapsedTime) {
        animations.get(currentFrameStrip).update(elapsedTime);
    }

    public Vector2f getTextureOffset() {
        return getCurrentFrame().getFrameOffset();
    }

    public void startAnimation() {
        getCurrentAnimation().start();
    }

    public void stopAnimation() {
        getCurrentAnimation().stop();
    }

    public void resetAnimation() {
        getCurrentAnimation().reset();
    }

    public void setCurrentFrameStrip(int frameStripNumber) {
        getCurrentAnimation().stop();
        currentFrameStrip = frameStripNumber;
        getCurrentAnimation().reset();
        getCurrentAnimation().start();
    }

    private FrameStripAnimation getCurrentAnimation() {
        return animations.get(currentFrameStrip);
    }

    private FramesStrip getCurrentFrameStrip() {
        return sprite.getFrameStrip(currentFrameStrip);
    }

    public FramesStrip.Frame getCurrentFrame() {
        return getCurrentFrameStrip().getFrame(getCurrentAnimation().getCurrentFrame());
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getSpriteRotateAngle() {
        return spriteRotateAngle;
    }

    public void setSpriteRotateAngle(float spriteRotateAngle) {
        this.spriteRotateAngle = spriteRotateAngle;
    }
}
