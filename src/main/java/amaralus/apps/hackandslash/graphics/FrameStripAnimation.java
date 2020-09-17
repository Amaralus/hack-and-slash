package amaralus.apps.hackandslash.graphics;

public class FrameStripAnimation {

    private final long timePerFrame;
    private final int framesCount;

    private int currentFrame;
    private boolean played;
    private long millis;

    public FrameStripAnimation(long animationTimeMs, int framesCount) {
        this.framesCount = framesCount;
        timePerFrame = animationTimeMs / framesCount;
    }

    public void update(long elapsedTime) {
        if (played) {
            millis += elapsedTime;
            if (millis >= timePerFrame) {
                millis = millis - timePerFrame;
                currentFrame++;
                if (currentFrame == framesCount)
                    reset();
            }
        }
    }

    public void start() {
        played = true;
    }

    public void stop() {
        played = false;
    }

    public void reset() {
        currentFrame = 0;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public boolean isPlayed() {
        return played;
    }
}
