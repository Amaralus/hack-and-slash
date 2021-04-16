package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.graphics.entities.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.entities.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.graphics.scene.Scene;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.springframework.stereotype.Service;

import static org.lwjgl.opengl.GL11.*;

@Service
public class RendererService {

    private final Window window;
    private final SpriteRenderer spriteRenderer;
    private final PrimitiveRender primitiveRender;

    private final Scene activeScene;

    public RendererService(Window window, PrimitiveRender primitiveRender, ResourceManager resourceManager) {
        this.window = window;
        activeScene = new Scene(window.getWidth(), window.getHeight());

        spriteRenderer = new SpriteRenderer(resourceManager);
        this.primitiveRender = primitiveRender;
    }

    public void render() {
        clear();

        var sceneLayers = activeScene.buildSceneGraphLayers();

        for (int i = sceneLayers.size() - 1; i >= 0; i--)
            for (var node : sceneLayers.get(i).getNodes())
                if (node instanceof Entity) {
                    var entity = (Entity) node;
                    var renderComponent = entity.getRenderComponent();

                    if (renderComponent instanceof SpriteRenderComponent)
                        spriteRenderer.render(activeScene.getCamera(), renderComponent.wrapTo(SpriteRenderComponent.class), entity.getGlobalPosition());
                    if (renderComponent instanceof Primitive)
                        primitiveRender.render(activeScene.getCamera(), renderComponent.wrapTo(Primitive.class), entity.getGlobalPosition());
                }

        window.swapBuffers();
    }

    private void clear() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public Scene getActiveScene() {
        return activeScene;
    }
}
