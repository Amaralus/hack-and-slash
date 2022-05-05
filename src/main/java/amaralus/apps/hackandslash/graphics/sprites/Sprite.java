package amaralus.apps.hackandslash.graphics.sprites;

import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.gpu.texture.Texture;
import amaralus.apps.hackandslash.io.data.SpriteSheetData;
import amaralus.apps.hackandslash.resources.Resource;
import lombok.Getter;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class Sprite implements Resource<String> {

    @Getter
    private final String resourceId;
    @Getter
    private final Texture texture;
    @Getter
    private final VertexArraysObject vao;
    @Getter
    private final Vector2f offsetToSpriteCenter;
    private final List<FramesStrip> framesStrips;

    public Sprite(String resourceId, Texture texture, VertexArraysObject vao, SpriteSheetData spriteSheetData) {
        this.resourceId = resourceId;
        this.texture = texture;
        this.vao = vao;

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

    public float getWidth() {
        return texture.getWidth();
    }

    public float getHeight() {
        return texture.getHeight();
    }

    public Vector2f getSize() {
        return vec2(getWidth(), getHeight());
    }

    @Override
    public void destroy() {
        // нет необходимости
    }

    public FramesStrip getFrameStrip(int frameStripNumber) {
        return framesStrips.get(frameStripNumber);
    }

    public List<FramesStrip> getFramesStrips() {
        return Collections.unmodifiableList(framesStrips);
    }
}
