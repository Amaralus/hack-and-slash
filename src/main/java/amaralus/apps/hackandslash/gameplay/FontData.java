package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.gpu.texture.Texture;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

public class FontData {

    private final Texture texture;
    private final VertexArraysObject vao;
    private final STBTTFontinfo info;
    private final STBTTBakedChar.Buffer cdata;
    private final int ascent;
    private final int descent;
    private final int lineGap;

    public FontData(Texture texture, VertexArraysObject vao, STBTTFontinfo info, STBTTBakedChar.Buffer cdata, int ascent, int descent, int lineGap) {
        this.texture = texture;
        this.vao = vao;
        this.info = info;
        this.cdata = cdata;
        this.ascent = ascent;
        this.descent = descent;
        this.lineGap = lineGap;
    }

    public Texture getTexture() {
        return texture;
    }

    public VertexArraysObject getVao() {
        return vao;
    }

    public int getWidth() {
        return texture.getWidth();
    }

    public int getHeight() {
        return texture.getHeight();
    }

    public STBTTFontinfo getInfo() {
        return info;
    }

    public STBTTBakedChar.Buffer getCdata() {
        return cdata;
    }

    public int getAscent() {
        return ascent;
    }

    public int getDescent() {
        return descent;
    }

    public int getLineGap() {
        return lineGap;
    }
}
