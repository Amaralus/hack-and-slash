package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.entities.gpu.Shader;
import amaralus.apps.hackandslash.graphics.entities.primitives.Line;
import amaralus.apps.hackandslash.resources.ResourceManager;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static org.lwjgl.opengl.GL11.*;

public class PrimitiveRender {

    private final Shader primitiveShader;

    public PrimitiveRender() {
        primitiveShader = getService(ResourceManager.class).getResource("primitive", Shader.class);
    }

    public void render(Line line) {
        primitiveShader.use();
        line.getVao().bind();
        glDrawArrays(GL_LINES, 0, 2);
        line.getVao().unbind();
    }
}
