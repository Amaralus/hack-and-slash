package amaralus.apps.hackandslash.io.entities;

public class FrameStripData {

    private final int framesCount;
    private final boolean animated;
    private final long animationTime;

    public FrameStripData(int framesCount, boolean animated, long animationTime) {
        this.framesCount = framesCount;
        this.animated = animated;
        this.animationTime = animationTime;
    }

    public FrameStripData(int framesCount) {
        this(framesCount, false, 0L);
    }

    public int getFramesCount() {
        return framesCount;
    }

    public boolean isAnimated() {
        return animated;
    }

    public long getAnimationTime() {
        return animationTime;
    }
}
