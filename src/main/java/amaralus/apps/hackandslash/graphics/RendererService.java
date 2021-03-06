package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import amaralus.apps.hackandslash.graphics.entities.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.entities.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.graphics.scene.Scene;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.joml.Vector2f;
import org.springframework.stereotype.Service;

import static org.lwjgl.opengl.GL11.*;

@Service
public class RendererService {

    private final Window window;
    private final SpriteRenderer spriteRenderer;
    private final PrimitiveRenderer primitiveRenderer;

    private final Scene activeScene;

    public RendererService(Window window, PrimitiveRenderer primitiveRenderer, ResourceManager resourceManager) {
        this.window = window;
        activeScene = new Scene(window.getWidth(), window.getHeight());

        spriteRenderer = new SpriteRenderer(resourceManager);
        this.primitiveRenderer = primitiveRenderer;
    }

    public void render() {
        clear();

        var sceneLayers = activeScene.buildSceneGraphLayers();

        for (int i = sceneLayers.size() - 1; i >= 0; i--)
            for (var node : sceneLayers.get(i).getNodes())
                if (node instanceof Entity) {
                    var entity = (Entity) node;
                    var renderComponent = entity.getRenderComponent();

                    doRender(renderComponent, entity.getGlobalPosition());
                }

        window.swapBuffers();
    }

    private void doRender(RenderComponent renderComponent, Vector2f globalPosition) {
        if (renderComponent.isNull())
            return;

        var camera = activeScene.getCamera();

        switch (renderComponent.getRenderComponentType()) {
            case PRIMITIVE:
                primitiveRenderer.render(camera, renderComponent.wrapTo(Primitive.class), globalPosition);
                break;
            case SPRITE:
                spriteRenderer.render(camera, renderComponent.wrapTo(SpriteRenderComponent.class), globalPosition);
                break;
            default:
        }
    }

    private void clear() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public Scene getActiveScene() {
        return activeScene;
    }
}
