package amaralus.apps.hackandslash.graphics.entities.sprites;

import amaralus.apps.hackandslash.common.Updateable;

public class AnimatedFramesStripContext extends FramesStripContext implements Updateable {

    private final long timePerFrame;

    private boolean played;
    private long millis;

    public AnimatedFramesStripContext(int framesCount, long animationTimeMs) {
        super(framesCount);
        timePerFrame = animationTimeMs / framesCount;
    }

    @Override
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

    @Override
    public boolean isAnimated() {
        return true;
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

    public boolean isPlayed() {
        return played;
    }
}
