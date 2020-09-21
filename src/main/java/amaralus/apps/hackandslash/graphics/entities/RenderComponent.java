package amaralus.apps.hackandslash.graphics.entities;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.graphics.entities.sprites.FramesStrip;
import amaralus.apps.hackandslash.graphics.entities.sprites.FramesStripContext;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.graphics.entities.sprites.Animation;
import org.joml.Vector2f;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RenderComponent implements Updateable {

    private final Sprite sprite;
    private final List<FramesStripContext> framesStripContextList;

    private int currentFrameStrip;
    private float spriteRotateAngle;

    public RenderComponent(Sprite sprite) {
        this.sprite = sprite;
        framesStripContextList = sprite.getFramesStrips().stream()
                .map(framesStrip -> {
                    var framesCount = framesStrip.getFramesCount();
                    if (framesStrip.isAnimated())
                        return new FramesStripContext(framesCount, new Animation(framesCount, framesStrip.getAnimationTimeMs()));
                    else
                        return new FramesStripContext(framesCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void update(long elapsedTime) {
        var animation = getCurrentFramesStripContext().getAnimation();
        if (animation != null) animation.update(elapsedTime);
    }

    public void computeAnimation(Consumer<Animation> animationConsumer) {
        getCurrentFramesStripContext().computeIfAnimated(animationConsumer);
    }

    public Vector2f getTextureOffset() {
        return getCurrentFrame().getFrameOffset();
    }

    public void setCurrentFrameStrip(int frameStripNumber) {
        currentFrameStrip = frameStripNumber;
    }

    public FramesStripContext getCurrentFramesStripContext() {
        return framesStripContextList.get(currentFrameStrip);
    }

    public FramesStrip getCurrentFrameStrip() {
        return sprite.getFrameStrip(currentFrameStrip);
    }

    public FramesStrip.Frame getCurrentFrame() {
        return getCurrentFrameStrip().getFrame(getCurrentFramesStripContext().getCurrentFrame());
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
