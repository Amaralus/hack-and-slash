package amaralus.apps.hackandslash.graphics.data.sprites;

import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.io.entities.FrameStripData;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public final class FramesStrip {

    private final int frameWidth;
    private final int frameHeight;
    private final boolean animated;
    private final List<Frame> frames;

    public FramesStrip(Texture texture, FrameStripData frameStripData, int frameWidth, int frameHeight, int frameStripNumber) {
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

    private float calcOffset(float textureLength, float frameLength, float frameNum) {
        return (frameLength * (frameNum - 1)) / textureLength;
    }

    public Frame getFrame(int frameNumber) {
        return frames.get(frameNumber);
    }

    public int getFramesCount() {
        return frames.size();
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public boolean isAnimated() {
        return animated;
    }

    public final class Frame {
        private final Vector2f frameOffset;

        public Frame(Vector2f frameOffset) {
            this.frameOffset = frameOffset;
        }

        public Vector2f getFrameOffset() {
            return frameOffset;
        }

        public int getFrameWidth() {
            return FramesStrip.this.getFrameWidth();
        }

        public int getFrameHeight() {
            return FramesStrip.this.getFrameHeight();
        }
    }
}
