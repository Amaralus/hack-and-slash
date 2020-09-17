package amaralus.apps.hackandslash.graphics.data.sprites;

import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.VertexArraysObject;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class SpriteSheet extends SimpleSprite {

    private final Vector2f offsetToSpriteCenter;
    private final List<FramesStrip> framesStrips;

    public SpriteSheet(Texture texture, VertexArraysObject vao, SpriteSheetData spriteSheetData) {
        super(texture, vao);

        int frameStripsCount = spriteSheetData.getFrameStrips().size();

        framesStrips = new ArrayList<>(frameStripsCount);
        for (int frameStripNumber = 0; frameStripNumber < frameStripsCount; frameStripNumber++)
            framesStrips.add(new FramesStrip(
                    texture,
                    spriteSheetData.getFrameStrips().get(frameStripNumber),
                    spriteSheetData.getFrameWidth(),
                    spriteSheetData.getFrameHeight(),
                    frameStripNumber));

        offsetToSpriteCenter = frameTexturePosition(texture, spriteSheetData).mul(0.5f);
    }

    public static Vector2f frameTexturePosition(Texture texture, SpriteSheetData spriteSheetData) {
        return vec2(
                (float) spriteSheetData.getFrameWidth() / (float) texture.getWidth(),
                (float) spriteSheetData.getFrameHeight() / (float) texture.getHeight());
    }

    @Override
    public Vector2f getOffsetToSpriteCenter() {
        return offsetToSpriteCenter;
    }

    public FramesStrip getFrameStrip(int frameStripNumber) {
        return framesStrips.get(frameStripNumber);
    }

    public List<FramesStrip> getFramesStrips() {
        return Collections.unmodifiableList(framesStrips);
    }
}
