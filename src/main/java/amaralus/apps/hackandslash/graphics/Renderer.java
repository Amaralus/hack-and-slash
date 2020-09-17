package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.services.Window;
import org.joml.Vector2f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;
    private final Camera camera;
    private final SpriteRenderer spriteRenderer;

    public Renderer(Window window) {
        this.window = window;
        camera = new Camera(window.getWidth(), window.getHeight());
        camera.setScale(4.5f);

        spriteRenderer = new SpriteRenderer(camera);
    }

    public void render(List<RenderEntity> renderEntities, Vector2f entityPos) {
        clear();

        for (var renderEntity : renderEntities)
            spriteRenderer.render(renderEntity, entityPos);

        window.swapBuffers();
    }

    private void clear() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
