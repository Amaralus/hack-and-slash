package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.entities.Shader;
import amaralus.apps.hackandslash.graphics.entities.data.Line;
import amaralus.apps.hackandslash.resources.ResourceManager;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static org.lwjgl.opengl.GL11.*;

public class LineRender {

    private final Shader lineShader;

    public LineRender() {
        lineShader = getService(ResourceManager.class).getResource("line", Shader.class);
    }

    public void render(Line line) {
        lineShader.use();
        line.getVao().bind();
        glDrawArrays(GL_LINES, 0, 2);
        line.getVao().unbind();
    }
}
