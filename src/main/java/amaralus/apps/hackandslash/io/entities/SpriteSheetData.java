package amaralus.apps.hackandslash.io.entities;

import java.util.List;

public class SpriteSheetData {

    private String name;
    private final int frameWidth;
    private final int frameHeight;
    private final List<FrameStripData> frameStrips;

    public SpriteSheetData(int frameWidth, int frameHeight, List<FrameStripData> frameStrips) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameStrips = frameStrips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
