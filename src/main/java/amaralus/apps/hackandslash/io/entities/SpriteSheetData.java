package amaralus.apps.hackandslash.io.entities;

import java.util.List;

public class SpriteSheetData {

    private int frameWidth;
    private int frameHeight;
    private List<Integer> framesCount;

    public SpriteSheetData(int frameWidth, int frameHeight, List<Integer> framesCount) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.framesCount = framesCount;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public List<Integer> getFramesCount() {
        return framesCount;
    }

    public void setFramesCount(List<Integer> framesCount) {
        this.framesCount = framesCount;
    }
}
