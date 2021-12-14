package amaralus.apps.hackandslash.graphics.sprites;

import java.util.function.Consumer;

public class FramesStripContext {

    private final int framesCount;
    private final boolean animated;
    private final Animation animation;

    private int currentFrame;

    public FramesStripContext(int framesCount) {
        this(framesCount, false, null);
    }

    public FramesStripContext(int framesCount, Animation animation) {
        this(framesCount, true, animation);
    }

    private FramesStripContext(int framesCount, boolean animated, Animation animation) {
        this.framesCount = framesCount;
        this.animated = animated;
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void computeIfAnimated(Consumer<Animation> animationConsumer) {
        if (animated) animationConsumer.accept(animation);
    }

    public int getFramesCount() {
        return framesCount;
    }

    public int getCurrentFrame() {
        return isAnimated() ? animation.getCurrentFrame() : currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame < 0 || currentFrame >= framesCount)
            throw new IllegalArgumentException("illegal frame number: " + currentFrame + " frames count: " + framesCount);
        this.currentFrame = currentFrame;
    }
}
