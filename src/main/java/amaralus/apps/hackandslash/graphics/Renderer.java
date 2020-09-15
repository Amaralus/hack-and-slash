package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.data.sprites.Sprite;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import amaralus.apps.hackandslash.services.Window;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

import java.util.List;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;
    private final Camera camera;
    private final SpriteRenderer spriteRenderer;

    public Renderer(Window window) {
        this.window = window;
        camera = new Camera(window.getWidth(), window.getHeight());
        camera.setScale(4.5f);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);


        spriteRenderer = new SpriteRenderer(getService(ResourceFactory.class).produceShader("texture"));
    }

    public void render(List<Sprite> sprites, Vector2f entityPos) {
        clear();

        for (Sprite sprite : sprites)
            spriteRenderer.draw(camera, sprite, entityPos, 0f);

        window.swapBuffers();
    }

    private void clear() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
