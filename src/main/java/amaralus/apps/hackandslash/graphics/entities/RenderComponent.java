package amaralus.apps.hackandslash.graphics.entities;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.graphics.entities.sprites.FrameStripAnimation;
import amaralus.apps.hackandslash.graphics.entities.sprites.FramesStrip;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import org.joml.Vector2f;

import java.util.List;
import java.util.stream.Collectors;

public class RenderComponent implements Updateable {

    private final Sprite sprite;
    private final List<FrameStripAnimation> animations;

    private int currentFrameStrip;
    private float spriteRotateAngle;

    public RenderComponent(Sprite sprite) {
        this.sprite = sprite;
        animations = sprite.getFramesStrips().stream()
                .map(framesStrip -> new FrameStripAnimation(1000L, framesStrip.getFramesCount()))
                .collect(Collectors.toList());
    }

    @Override
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
