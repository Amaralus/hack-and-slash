package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.entities.gpu.Shader;
import amaralus.apps.hackandslash.graphics.entities.primitives.Line;
import amaralus.apps.hackandslash.graphics.entities.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.entities.primitives.Triangle;
import amaralus.apps.hackandslash.resources.ResourceManager;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static org.lwjgl.opengl.GL11.*;

public class PrimitiveRender {

    private final Shader primitiveShader;

    public PrimitiveRender() {
        primitiveShader = getService(ResourceManager.class).getResource("primitive", Shader.class);
    }

    public void render(Primitive primitive) {
        primitiveShader.use();
        primitive.getVao().bind();

        if (primitive instanceof Line)
            renderLine();
        else if (primitive instanceof Triangle)
            renderTriangle();

        primitive.getVao().unbind();
    }

    private void renderLine() {
        glDrawArrays(GL_LINES, 0, 2);
    }

    private void renderTriangle() {
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
