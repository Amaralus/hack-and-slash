package amaralus.apps.hackandslash.graphics.font;

import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.gpu.texture.Texture;
import amaralus.apps.hackandslash.resources.Resource;
import lombok.Getter;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

@Getter
public class Font extends Resource {

    private final STBTTFontinfo fontInfo;
    private final STBTTBakedChar.Buffer bakedChars;
    private final FontVerticalMetrics verticalMetrics;
    private final Texture texture;
    private final VertexArraysObject vao;

    public Font(String resourceName,
                STBTTFontinfo fontInfo,
                STBTTBakedChar.Buffer bakedChars,
                FontVerticalMetrics verticalMetrics,
                Texture texture,
                VertexArraysObject vao) {
        super(resourceName);
        this.fontInfo = fontInfo;
        this.bakedChars = bakedChars;
        this.verticalMetrics = verticalMetrics;
        this.texture = texture;
        this.vao = vao;
    }

    @Override
    public void destroy() {
        texture.destroy();
        vao.destroy();
    }
}
