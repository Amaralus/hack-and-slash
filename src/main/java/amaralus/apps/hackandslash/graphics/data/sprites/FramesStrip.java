package amaralus.apps.hackandslash.graphics.data.sprites;

import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.io.entities.FrameStripData;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class FramesStrip {

    private final float frameWidth;
    private final float frameHeight;
    private final boolean animated;
    private final List<Frame> frames;

    private int currentFrame = 0;

    public FramesStrip(Texture texture, FrameStripData frameStripData, int frameWidth, int frameHeight,  int frameStripNumber) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        animated = frameStripData.isAnimated();

        int framesCount = frameStripData.getFramesCount();
        frames = new ArrayList<>(framesCount);

        for (int frameNumber = 0; frameNumber < framesCount; frameNumber++) {
            var frameOffset = vec2(
                    calcOffset(texture.getWidth(), frameWidth, (frameNumber + 1)),
                    calcOffset(texture.getHeight(), frameHeight, (frameStripNumber + 1)));
            frames.add(new Frame(frameOffset));
        }
    }

    public void reset() {
        currentFrame = 0;
    }

    public void nextFrame() {
        currentFrame++;
        if (currentFrame == frames.size())
            reset();
    }

    public Frame getCurrentFrame() {
        return frames.get(currentFrame);
    }

    private float calcOffset(float textureLength, float frameLength, float frameNum) {
        return (frameLength * (frameNum - 1)) / textureLength;
    }

    public float getFrameWidth() {
        return frameWidth;
    }

    public float getFrameHeight() {
        return frameHeight;
    }

    public boolean isAnimated() {
        return animated;
    }

    public class Frame {
        private final Vector2f frameOffset;

        public Frame(Vector2f frameOffset) {
            this.frameOffset = frameOffset;
        }

        public Vector2f getFrameOffset() {
            return frameOffset;
        }

        public float getFrameWidth() {
            return FramesStrip.this.getFrameWidth();
        }

        public float getFrameHeight() {
            return FramesStrip.this.getFrameHeight();
        }
    }
}
