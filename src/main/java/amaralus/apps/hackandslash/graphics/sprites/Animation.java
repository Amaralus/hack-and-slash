package amaralus.apps.hackandslash.graphics.sprites;

import amaralus.apps.hackandslash.common.Updatable;

public class Animation implements Updatable {

    private final int framesCount;
    private final long timePerFrame;

    private boolean played;
    private boolean reversed;
    private boolean cycled = true;
    private int currentFrame;
    private long millis;

    public Animation(int framesCount, long animationTimeMs) {
        this.framesCount = framesCount;
        timePerFrame = animationTimeMs / framesCount;
    }

    @Override
    public void update(long elapsedTime) {
        if (played) {
            millis += elapsedTime;
            if (millis >= timePerFrame) {
                millis = millis - timePerFrame;

                nextFrame();

                if (isEndFrame()) {
                    if (cycled)
                        reset();
                    else {
                        stop();
                        prevFrame();
                    }
                }
            }
        }
    }

    private void nextFrame() {
        if (reversed)
            currentFrame--;
        else
            currentFrame++;
    }

    private void prevFrame() {
        if (reversed)
            currentFrame++;
        else
            currentFrame--;
    }

    private boolean isEndFrame() {
        return reversed ? currentFrame == -1 : currentFrame == framesCount;
    }

    public void start() {
        played = true;
    }

    public void stop() {
        played = false;
    }

    public void reset() {
        if (reversed)
            currentFrame = framesCount - 1;
        else
            currentFrame = 0;
    }

    public void resetAndStart() {
        reset();
        start();
    }

    public void stopAndReset() {
        stop();
        reset();
    }

    public boolean isPlayed() {
        return played;
    }

    public void revere() {
        reversed = !reversed;
    }

    public boolean isReversed() {
        return reversed;
    }

    public boolean isCycled() {
        return cycled;
    }

    public void setCycled(boolean cycled) {
        this.cycled = cycled;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }
}
