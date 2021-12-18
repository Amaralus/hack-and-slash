package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.gameplay.FontData;
import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.gpu.shader.Shader;
import amaralus.apps.hackandslash.graphics.scene.Camera;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.joml.Vector2f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static amaralus.apps.hackandslash.utils.BufferUtil.bufferOf;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_GetCodepointKernAdvance;
import static org.lwjgl.system.MemoryStack.stackPush;

public class FontRenderer {

    private final Shader fontShader;
    private FontData fontData;
    private final boolean kerningEnabled = false;
    private final int fontHeight = 24;

    public FontRenderer(ResourceManager resourceManager) {
        fontShader = resourceManager.getResource("font", Shader.class);
    }

    public void setFontData(FontData fontData) {
        this.fontData = fontData;
    }

    public void renderText(String text, Color color, Vector2f textPosition, Camera camera) {

            fontShader.use();
            fontShader.setVec3("fontColor", color.rgb());
            fontShader.setMat4("projection", camera.getProjection());
            var model = mat4().translate(vec3(copy(textPosition).sub(camera.getLeftTopPosition()), 0f));
            fontShader.setMat4("model", model);

            var vao = fontData.getVao();
            vao.bind();
            fontData.getTexture().bind();

            renderText(text, textPosition);

            vao.unbind();
    }

    private void renderText(String text, Vector2f textPosition) {
        try (MemoryStack stack = stackPush()) {

            var x = stack.floats(textPosition.x());
            var y = stack.floats(textPosition.y());
            var alignedQuad = STBTTAlignedQuad.mallocStack(stack);
            var pCodePoint = stack.mallocInt(1);

            for (int i = 0, to = text.length(); i < to; ) {
                i += getCodePoint(text, to, i, pCodePoint);

                int cp = pCodePoint.get(0);

                if (cp < 32) continue;

                stbtt_GetBakedQuad(fontData.getCdata(), fontData.getWidth(), fontData.getHeight(), cp - 32, x, y, alignedQuad, true);
                x.put(0, x.get(0));

                if (kerningEnabled && i < to) {
                    getCodePoint(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(fontData.getInfo(), cp, pCodePoint.get(0)));
                }

                renderCharacter(alignedQuad);
            }
        }
    }

    private void renderCharacter(STBTTAlignedQuad alignedQuad) {
        fontData.getVao().getBuffers().get(0).updateBuffer(bufferOf(
                alignedQuad.x0(), alignedQuad.y0(), alignedQuad.s0(), alignedQuad.t0(),
                alignedQuad.x1(), alignedQuad.y0(), alignedQuad.s1(), alignedQuad.t0(),
                alignedQuad.x1(), alignedQuad.y1(), alignedQuad.s1(), alignedQuad.t1(),
                alignedQuad.x0(), alignedQuad.y1(), alignedQuad.s0(), alignedQuad.t1()));

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    private float nextLine(FloatBuffer y) {
        float lineY = y.get(0) + (fontData.getAscent() - fontData.getDescent() + fontData.getLineGap());
        y.put(0, lineY);
        return lineY;
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
