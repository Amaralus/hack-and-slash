package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.gameplay.Entity;
import amaralus.apps.hackandslash.graphics.entities.Camera;
import amaralus.apps.hackandslash.graphics.entities.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.scene.Scene;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@Service
public class Renderer {

    @Lazy
    private final Window window;
    private final Camera camera;
    private final SpriteRenderer spriteRenderer;
    private final PrimitiveRender primitiveRender;

    public Renderer(Window window, PrimitiveRender primitiveRender, ResourceManager resourceManager) {
        this.window = window;
        camera = new Camera(window.getWidth(), window.getHeight());
        camera.setScale(0.5f);

        spriteRenderer = new SpriteRenderer(resourceManager);
        this.primitiveRender = primitiveRender;
    }

    @Deprecated
    public void render(List<Entity> entities, List<Primitive> primitives) {
        clear();

        for (Entity entity : entities)
            spriteRenderer.render(camera, entity.getRenderComponent(), entity.getPosition());

        for (var primitive : primitives)
            primitiveRender.render(primitive);

        window.swapBuffers();
    }

    public void render(Scene scene) {
        clear();

        var sceneLayers = scene.buildSceneGraphLayers();

        for (int i = sceneLayers.size() - 1; i >= 0; i--)
            for (var node : sceneLayers.get(i).getNodes())
                if (node instanceof Entity) {
                    var entity = (Entity) node;
                    spriteRenderer.render(scene.getCamera(), entity.getRenderComponent(), entity.getPosition());
                }

        window.swapBuffers();
    }

    private void clear() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public Camera getCamera() {
        return camera;
    }
}
