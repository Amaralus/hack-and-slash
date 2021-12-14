package amaralus.apps.hackandslash.graphics.sprites;

import amaralus.apps.hackandslash.graphics.rendering.RenderComponent;
import org.joml.Vector2f;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static amaralus.apps.hackandslash.graphics.rendering.RenderComponent.RenderComponentType.SPRITE;

public class SpriteRenderComponent implements RenderComponent {

    private final Sprite sprite;
    private final List<FramesStripContext> framesStripContextList;

    private int currentFrameStrip;
    private float spriteRotateAngle;

    public SpriteRenderComponent(Sprite sprite) {
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

    public void changeAnimatedFrameStrip(int frameStripNumber) {
        if (currentFrameStrip == frameStripNumber) return;
        computeAnimation(Animation::stopAndReset);
        setCurrentFrameStrip(frameStripNumber);
        computeAnimation(Animation::start);
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

    public void addSpriteRotateAngle(float spriteRotateAngle) {
        setSpriteRotateAngle(this.spriteRotateAngle + spriteRotateAngle);
    }

    @Override
    public RenderComponentType getRenderComponentType() {
        return SPRITE;
    }
}
