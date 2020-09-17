package amaralus.apps.hackandslash.io.entities;

public class FrameStripData {

    private final int framesCount;
    private final boolean animated;

    public FrameStripData(int framesCount, boolean animated) {
        this.framesCount = framesCount;
        this.animated = animated;
    }

    public int getFramesCount() {
        return framesCount;
    }

    public boolean isAnimated() {
        return animated;
    }
}
