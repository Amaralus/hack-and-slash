package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.scene.SceneManager;
import org.joml.Vector2f;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

@Service
public class RenderingService {

    private final Window window;
    private final SceneManager sceneManager;
    private final Map<RenderComponentType, Renderer> renderers;

    public RenderingService(Window window, SceneManager sceneManager, Map<RenderComponentType, Renderer> renderers) {
        this.window = window;
        this.sceneManager = sceneManager;
        this.renderers = renderers;
    }

    public void render() {
        clear();

        var sceneLayers = sceneManager.getActiveScene().getSceneGraph().buildSceneGraphLayers();

        for (int i = sceneLayers.size() - 1; i >= 0; i--)
            for (var node : sceneLayers.get(i).getNodes())
                if (node instanceof Entity) {
                    var entity = (Entity) node;
                    var renderComponent = entity.getRenderComponent();

                    doRender(renderComponent, entity.getPhysicalComponent().getPosition());
                }

        window.swapBuffers();
    }

    private void doRender(RenderComponent renderComponent, Vector2f globalPosition) {
        if (renderComponent.isNull())
            return;

        var camera = sceneManager.getActiveScene().getCamera();

        var renderer = renderers.get(renderComponent.getRenderComponentType());
        if (renderer != null)
            renderer.render(camera, renderComponent, globalPosition);
    }

    private void clear() {
        var background = sceneManager.getActiveScene().getBackgroundColor();
        glClearColor(background.r(), background.g(), background.b(), background.a());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
