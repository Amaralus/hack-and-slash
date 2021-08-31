package amaralus.apps.hackandslash.io.entities;

import java.util.List;

public class ResourcesData {

    private final List<SpriteSheetData> sprites;

    public ResourcesData(List<SpriteSheetData> sprites) {
        this.sprites = sprites;
    }

    public List<SpriteSheetData> getSprites() {
        return sprites;
    }
}
