package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.gameplay.FontData;
import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.gpu.shader.Shader;
import amaralus.apps.hackandslash.graphics.scene.Camera;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_GetCodepointKernAdvance;
import static org.lwjgl.system.MemoryStack.stackPush;

public class FontRenderer {

    private final Shader fontShader;
    private FontData fontData;
    private boolean kerningEnabled = true;

    public FontRenderer(ResourceManager resourceManager) {
        fontShader = resourceManager.getResource("font", Shader.class);
    }

    public void setFontData(FontData fontData) {
        this.fontData = fontData;
    }

    public void renderText(String text, Color color, Camera camera) {
        try (MemoryStack stack = stackPush()) {

            var pCodePoint = stack.mallocInt(1);

            var x = stack.floats(0.0f);
            var y = stack.floats(0.0f);

            var alignedQuad = STBTTAlignedQuad.mallocStack(stack);

            float factorX = 1.0f;
            float factorY = 1.0f;

            // положение текста (скорее всего пиксели)
            float lineY = 50f;
            y.put(0, lineY);

            fontShader.use();
            fontShader.setVec3("fontColor", color.rgb());
            fontShader.setMat4("projection", camera.getProjection());
            var vao = fontData.getVao();
            vao.bind();
            fontData.getTexture().bind();

            for (int i = 0, to = text.length(); i < to; ) {
                i += getCodePoint(text, to, i, pCodePoint);

                int cp = pCodePoint.get(0);

                if (cp < 32) continue;

                float cpX = x.get(0);
                stbtt_GetBakedQuad(fontData.getCdata(), fontData.getWidth(), fontData.getHeight(), cp - 32, x, y, alignedQuad, true);
                x.put(0, scale(cpX, x.get(0), factorX));
                if (kerningEnabled && i < to) {
                    getCodePoint(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(fontData.getInfo(), cp, pCodePoint.get(0)));
                }

                float x0 = scale(cpX, alignedQuad.x0(), factorX);
                float x1 = scale(cpX, alignedQuad.x1(), factorX);
                float y0 = scale(lineY, alignedQuad.y0(), factorY);
                float y1 = scale(lineY, alignedQuad.y1(), factorY);

                vao.getBuffers().get(0).updateBuffer(
                        FloatBuffer.wrap(new float[]{
                                alignedQuad.s0(), alignedQuad.t0(), x0, y0,
                                alignedQuad.s1(), alignedQuad.t0(), x1, y0,
                                alignedQuad.s1(), alignedQuad.t1(), x1, y1,
                                alignedQuad.s0(), alignedQuad.t1(), x0, y1
                        }),
                        0);
                fontData.getTexture().bind();
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            }
            vao.unbind();
        }
    }

    private float scale(float center, float offset, float factor) {
        return (offset - center) * factor + center;
    }

    private int getCodePoint(String text, int to, int i, IntBuffer codePointOut) {
        char c1 = text.charAt(i);
        if (Character.isHighSurrogate(c1) && i + 1 < to) {
            char c2 = text.charAt(i + 1);
            if (Character.isLowSurrogate(c2)) {
                codePointOut.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        codePointOut.put(0, c1);
        return 1;
    }
}
