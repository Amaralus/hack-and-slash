package amaralus.apps.hackandslash.io.entities;

import java.util.List;

public class SpriteSheetData {

    private int textureWidth;
    private int textureHeight;
    private List<Integer> texturesCount;

    public SpriteSheetData(int textureWidth, int textureHeight, List<Integer> texturesCount) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texturesCount = texturesCount;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public void setTextureWidth(int textureWidth) {
        this.textureWidth = textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public void setTextureHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    public List<Integer> getTexturesCount() {
        return texturesCount;
    }

    public void setTexturesCount(List<Integer> texturesCount) {
        this.texturesCount = texturesCount;
    }

    @Override
    public String toString() {
        return "SpriteSheetData{" +
                "textureWidth=" + textureWidth +
                ", textureHeight=" + textureHeight +
                ", texturesCount=" + texturesCount +
                '}';
    }
}
