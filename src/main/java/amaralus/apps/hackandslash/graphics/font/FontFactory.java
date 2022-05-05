package amaralus.apps.hackandslash.graphics.font;

import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.gpu.buffer.repository.VaoRepository;
import amaralus.apps.hackandslash.graphics.gpu.buffer.repository.VboRepository;
import amaralus.apps.hackandslash.graphics.gpu.texture.Texture;
import amaralus.apps.hackandslash.graphics.gpu.texture.TextureFactory;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.data.ImageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferType.ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferUsage.DYNAMIC_DRAW;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.factory.VaoFactory.newVao;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.factory.VboFactory.floatBuffer;
import static java.nio.FloatBuffer.allocate;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_InitFont;

@Component
@RequiredArgsConstructor
@Slf4j
public class FontFactory {

    private static final int BITMAP_WIDTH = 1024;
    private static final int BITMAP_HEIGHT = 1024;
    private static final int BACKED_CHARACTERS_BUFFER_CAPACITY = 4096;
    private static final float FONT_HEIGHT_IN_PIXELS = 32;

    private final FontRepository fontRepository;
    private final VaoRepository vaoRepository;
    private final VboRepository vboRepository;
    private final FileLoadService fileLoadService;
    private final TextureFactory textureFactory;

    public void produceFont(String fontName) {
        log.debug("Загрузка шрифта {}", fontName);

        var fontData = fileLoadService.loadFontData("fonts/" + fontName + ".ttf");

        var fontInfo = initFontInfo(fontData);
        var metrics = new FontVerticalMetrics(fontInfo);

        var bakedChars = STBTTBakedChar.malloc(BACKED_CHARACTERS_BUFFER_CAPACITY);
        var texture = createFontTexture(fontName, fontData, bakedChars);

        var vao = createVao(fontName);

        var font = new Font(fontName, fontInfo, bakedChars, metrics, texture, vao);
        fontRepository.save(font);
    }

    private STBTTFontinfo initFontInfo(ByteBuffer fontData) {
        var info = STBTTFontinfo.create();
        if (!stbtt_InitFont(info, fontData))
            throw new IllegalStateException("Failed to initialize font information.");

        return info;
    }

    private Texture createFontTexture(String textureName, ByteBuffer ttf, STBTTBakedChar.Buffer bakedChars) {
        var pixels = BufferUtils.createByteBuffer(BITMAP_WIDTH * BITMAP_HEIGHT);

        stbtt_BakeFontBitmap(ttf, FONT_HEIGHT_IN_PIXELS, pixels, BITMAP_WIDTH, BITMAP_HEIGHT, 32, bakedChars);

        return textureFactory.produceFontTexture(textureName, new ImageData(BITMAP_WIDTH, BITMAP_HEIGHT, pixels));
    }

    private VertexArraysObject createVao(String fontName) {
        return newVao()
                .buffer(vboRepository.get("defaultTextureEbo"))
                .buffer(floatBuffer(allocate(16))
                        .type(ARRAY_BUFFER)
                        .usage(DYNAMIC_DRAW)
                        .saveAsVbo(fontName, vboRepository)
                        .dataFormat(0, 4, 4, 0, Float.TYPE))
                .saveAsVao(fontName, vaoRepository)
                .build();
    }
}
