package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.graphics.font.Font;
import amaralus.apps.hackandslash.graphics.font.FontRenderComponent;
import amaralus.apps.hackandslash.graphics.font.FontVerticalMetrics;
import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.gpu.shader.Shader;
import amaralus.apps.hackandslash.scene.Camera;
import org.joml.Vector2f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static amaralus.apps.hackandslash.utils.BufferUtil.bufferOf;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.copy;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.mat4;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec3;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_GetCodepointKernAdvance;
import static org.lwjgl.system.MemoryStack.stackPush;

public class FontRenderer {

    private final Shader fontShader;
    private boolean kerningEnabled = false;

    public FontRenderer(Shader fontShader) {
        this.fontShader = fontShader;
    }

    public void renderText(Camera camera, FontRenderComponent renderComponent, Vector2f textPosition) {
        var font = renderComponent.getFont();

        fontShader.use();
        fontShader.setVec3("fontColor", renderComponent.getColor().rgb());
        fontShader.setMat4("projection", camera.getProjection());
        var model = mat4().translate(vec3(copy(textPosition).sub(camera.getLeftTopPosition()), 0f));
        fontShader.setMat4("model", model);

        var vao = font.getVao();
        vao.bind();
        font.getTexture().bind();

        renderText(font, renderComponent.getText(), textPosition);

        vao.unbind();
    }

    private void renderText(Font font, String text, Vector2f textPosition) {
        try (MemoryStack stack = stackPush()) {

            var x = stack.floats(textPosition.x());
            var y = stack.floats(textPosition.y());
            var alignedQuad = STBTTAlignedQuad.mallocStack(stack);
            var pCodePoint = stack.mallocInt(1);

            for (int i = 0, to = text.length(); i < to; ) {
                i += getCodePoint(text, to, i, pCodePoint);

                int cp = pCodePoint.get(0);

                if (cp < 32) continue;

                stbtt_GetBakedQuad(font.getBakedChars(), font.getTexture().getWidth(), font.getTexture().getHeight(), cp - 32, x, y, alignedQuad, true);

                x.put(0, x.get(0));

                if (kerningEnabled && i < to) {
                    getCodePoint(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(font.getFontInfo(), cp, pCodePoint.get(0)));
                }

                renderCharacter(font.getVao(), alignedQuad);
            }
        }
    }

    private void renderCharacter(VertexArraysObject vao, STBTTAlignedQuad alignedQuad) {
        vao.getBuffers().get(0).updateBuffer(bufferOf(
                alignedQuad.x0(), alignedQuad.y0(), alignedQuad.s0(), alignedQuad.t0(),
                alignedQuad.x1(), alignedQuad.y0(), alignedQuad.s1(), alignedQuad.t0(),
                alignedQuad.x1(), alignedQuad.y1(), alignedQuad.s1(), alignedQuad.t1(),
                alignedQuad.x0(), alignedQuad.y1(), alignedQuad.s0(), alignedQuad.t1()));

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    private float nextLine(FloatBuffer y, FontVerticalMetrics metrics) {
        float lineY = y.get(0) + (metrics.getAscent() - metrics.getDescent() + metrics.getLineGap());
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

    public void setKerningEnabled(boolean kerningEnabled) {
        this.kerningEnabled = kerningEnabled;
    }
}
