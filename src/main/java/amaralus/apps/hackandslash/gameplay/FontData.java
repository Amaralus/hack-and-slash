package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.entities.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.entities.gpu.texture.Texture;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

public class FontData {

    private final int width;
    private final int height;
    private final STBTTFontinfo info;
    private final STBTTBakedChar.Buffer cdata;
    private final int ascent;
    private final int descent;
    private final int lineGap;

    public FontData(int width, int height, STBTTFontinfo info, STBTTBakedChar.Buffer cdata, int ascent, int descent, int lineGap) {
        this.width = width;
        this.height = height;
        this.info = info;
        this.cdata = cdata;
        this.ascent = ascent;
        this.descent = descent;
        this.lineGap = lineGap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
