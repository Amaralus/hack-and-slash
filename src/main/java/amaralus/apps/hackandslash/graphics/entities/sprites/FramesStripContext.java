package amaralus.apps.hackandslash.graphics.entities.sprites;

public class FramesStripContext {

    protected final int framesCount;
    protected int currentFrame;

    public FramesStripContext(int framesCount) {
        this.framesCount = framesCount;
    }

    public boolean isAnimated() {
        return false;
    }

    public int getFramesCount() {
        return framesCount;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame < 0 || currentFrame >= framesCount)
            throw new IllegalArgumentException("illegal frame number: " + currentFrame + " frames count: " + framesCount);
        this.currentFrame = currentFrame;
    }
}
