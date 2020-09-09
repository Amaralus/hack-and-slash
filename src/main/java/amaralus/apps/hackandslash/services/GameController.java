package amaralus.apps.hackandslash.services;

import amaralus.apps.hackandslash.Window;
import amaralus.apps.hackandslash.graphics.Camera;
import amaralus.apps.hackandslash.graphics.SpriteRenderer;
import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.sprites.SimpleSprite;
import amaralus.apps.hackandslash.graphics.data.sprites.SpriteSheet;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.KeyEvent;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

import static amaralus.apps.hackandslash.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameController {

    private final boolean[] keys = new boolean[1024];
    private final Window window;

    private final Vector2f entityWorldPos = vec2(0f, 0f);
    private final Camera camera;
    private SpriteSheet sprite;

    public GameController(Window window) {
        this.window = window;
        camera = new Camera(window.getWidth(), window.getHeight());
        window.setKeyCallBack(this::updateKeyEvent);
    }

    public void updateKeyEvent(KeyEvent event) {
        if (GLFW_PRESS == event.getAction())
            keys[event.getKey()] = true;
        if (GLFW_RELEASE == event.getAction())
            keys[event.getKey()] = false;
    }

    public void gameLoop() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        camera.setScale(4.5f);
        var spriteRenderer = new SpriteRenderer();

        var spriteSheetData = new FileLoadService().loadFromJson("sprites/data/testTextureSheet.json", SpriteSheetData.class);
        sprite = new SpriteSheet(new Texture("testTextureSheet"), spriteSheetData);
        var sprite2 = new SimpleSprite(new Texture("inosuke2"));

        long lastMillis = System.currentTimeMillis();
        while (!window.isShouldClose()) {
            glfwPollEvents();
            handleKeyActions();

            glClearColor(0f, 0f, 0f, 1f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            var millis = System.currentTimeMillis();

            if (lastMillis + 300 < millis) {
                lastMillis = millis;
                sprite.getActiveSprite().nextFrame();
            }

            spriteRenderer.draw(camera, sprite, entityWorldPos, 0f);
            spriteRenderer.draw(camera, sprite2, vec2(0f, 0f), 0f);

            window.swapBuffers();
        }

        sprite.destroy();
        sprite2.destroy();
    }

    private void handleKeyActions() {
        float speed = 5;
        if (keys[GLFW_KEY_W]) entityWorldPos.y -= speed;
        if (keys[GLFW_KEY_S]) entityWorldPos.y += speed;
        if (keys[GLFW_KEY_A]) entityWorldPos.x -= speed;
        if (keys[GLFW_KEY_D]) entityWorldPos.x += speed;

        if (keys[GLFW_KEY_I]) camera.moveUp(speed);
        if (keys[GLFW_KEY_K]) camera.moveDown(speed);
        if (keys[GLFW_KEY_J]) camera.moveLeft(speed);
        if (keys[GLFW_KEY_L]) camera.moveRight(speed);

        if (keys[GLFW_KEY_1]) sprite.setActiveSprite(0);
        if (keys[GLFW_KEY_2]) sprite.setActiveSprite(1);
        if (keys[GLFW_KEY_3]) sprite.setActiveSprite(2);
    }
}
