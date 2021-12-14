package amaralus.apps.hackandslash.graphics.entities.sprites;

import amaralus.apps.hackandslash.graphics.entities.gpu.Texture;
import amaralus.apps.hackandslash.graphics.entities.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.io.data.SpriteSheetData;
import amaralus.apps.hackandslash.resources.Resource;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class Sprite extends Resource {

    private final Texture texture;
    private final VertexArraysObject vao;
    private final Vector2f offsetToSpriteCenter;
    private final List<FramesStrip> framesStrips;

    public Sprite(String resourceName, Texture texture, VertexArraysObject vao, SpriteSheetData spriteSheetData) {
        super(resourceName);
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

    public Texture getTexture() {
        return texture;
    }

    public VertexArraysObject getVao() {
        return vao;
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

    public Vector2f getOffsetToSpriteCenter() {
        return offsetToSpriteCenter;
    }

    @Override
    public void destroy() {
        vao.destroy();
    }

    public FramesStrip getFrameStrip(int frameStripNumber) {
        return framesStrips.get(frameStripNumber);
    }

    public List<FramesStrip> getFramesStrips() {
        return Collections.unmodifiableList(framesStrips);
    }
}
