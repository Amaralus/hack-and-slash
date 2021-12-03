package amaralus.apps.hackandslash.io.data;

import java.util.List;

public class SpriteSheetData {

    private final int frameWidth;
    private final int frameHeight;
    private final List<FrameStripData> frameStrips;

    public SpriteSheetData(int frameWidth, int frameHeight, List<FrameStripData> frameStrips) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameStrips = frameStrips;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public List<FrameStripData> getFrameStrips() {
        return frameStrips;
    }
}
