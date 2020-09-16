package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.data.sprites.Sprite;

public class RenderEntity {

    private final Sprite sprite;

    public RenderEntity(Sprite sprite) {
        this.sprite = sprite;
    }



    public Sprite getSprite() {
        return sprite;
    }
}
