package amaralus.apps.hackandslash.graphics.font;

import lombok.Getter;
import org.lwjgl.stb.STBTTFontinfo;

import static org.lwjgl.stb.STBTruetype.stbtt_GetFontVMetrics;
import static org.lwjgl.system.MemoryStack.stackPush;

@Getter
public class FontVerticalMetrics {

    private final int ascent;
    private final int descent;
    private final int lineGap;

    public FontVerticalMetrics(STBTTFontinfo fontInfo) {
        try (var stack = stackPush()) {
            var pAscent = stack.mallocInt(1);
            var pDescent = stack.mallocInt(1);
            var pLineGap = stack.mallocInt(1);

            stbtt_GetFontVMetrics(fontInfo, pAscent, pDescent, pLineGap);

            ascent = pAscent.get(0);
            descent = pDescent.get(0);
            lineGap = pLineGap.get(0);
        }
    }
}
