package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.gameplay.Entity;
import amaralus.apps.hackandslash.graphics.entities.Camera;
import amaralus.apps.hackandslash.graphics.entities.data.Line;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;
    private final Camera camera;
    private final SpriteRenderer spriteRenderer;
    private final LineRender lineRender;

    public Renderer(Window window) {
        this.window = window;
        camera = new Camera(window.getWidth(), window.getHeight());
        camera.setScale(0.5f);

        spriteRenderer = new SpriteRenderer(camera);
        lineRender = new LineRender();
    }

    public void render(List<Entity> entities, Line line) {
        clear();

        for (Entity entity : entities) {
            spriteRenderer.render(entity.getRenderComponent(), entity.getPosition());
        }

        lineRender.render(line);

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
