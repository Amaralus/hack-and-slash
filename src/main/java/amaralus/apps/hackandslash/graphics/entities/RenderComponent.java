package amaralus.apps.hackandslash.graphics.entities;

import amaralus.apps.hackandslash.common.Updateable;
import amaralus.apps.hackandslash.graphics.entities.sprites.AnimatedFramesStripContext;
import amaralus.apps.hackandslash.graphics.entities.sprites.FramesStrip;
import amaralus.apps.hackandslash.graphics.entities.sprites.FramesStripContext;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
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
                    if (framesStrip.isAnimated())
                        return new AnimatedFramesStripContext(framesStrip.getFramesCount(), framesStrip.getAnimationTimeMs());
                    else
                        return new FramesStripContext(framesStrip.getFramesCount());
                })
                .collect(Collectors.toList());
    }

    @Override
    public void update(long elapsedTime) {
        doIfContextAnimated(context -> context.update(elapsedTime));
    }

    public Vector2f getTextureOffset() {
        return getCurrentFrame().getFrameOffset();
    }

    public void startAnimation() {
        doIfContextAnimated(AnimatedFramesStripContext::start);
    }

    public void stopAnimation() {
        doIfContextAnimated(AnimatedFramesStripContext::stop);
    }

    public void resetAnimation() {
        doIfContextAnimated(AnimatedFramesStripContext::reset);
    }

    public void setCurrentFrameStrip(int frameStripNumber) {
        startAnimation();
        resetAnimation();
        currentFrameStrip = frameStripNumber;
        startAnimation();
    }

    private void doIfContextAnimated(Consumer<AnimatedFramesStripContext> contextConsumer) {
        var context = getCurrentFramesStripContext();
        if (context.isAnimated())
            contextConsumer.accept((AnimatedFramesStripContext) context);
    }

    private FramesStripContext getCurrentFramesStripContext() {
        return framesStripContextList.get(currentFrameStrip);
    }

    private FramesStrip getCurrentFrameStrip() {
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
