package amaralus.apps.hackandslash.services;

import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.sprites.SpriteSheet;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.KeyEvent;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import org.joml.Vector2f;

import java.util.List;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;

public class GameController {

    private final boolean[] keys = new boolean[1024];
    private final Window window;
    private final Renderer renderer;

    private final Vector2f entityWorldPos = vec2(0f, 0f);
    private SpriteSheet sprite;
    private long lastMillis = System.currentTimeMillis();

    public GameController(Window window) {
        this.window = window;
        renderer = new Renderer(window);
        window.setKeyCallBack(this::updateKeyEvent);
    }

    public void updateKeyEvent(KeyEvent event) {
        if (GLFW_PRESS == event.getAction())
            keys[event.getKey()] = true;
        if (GLFW_RELEASE == event.getAction())
            keys[event.getKey()] = false;
    }

    public void gameLoop() {

        var gameLoop = new GameLoop(10L) {

            @Override
            public void onEnable() {
                var spriteSheetData = new FileLoadService().loadFromJson("sprites/data/testTextureSheet.json", SpriteSheetData.class);
                sprite = new SpriteSheet(new Texture("testTextureSheet"), spriteSheetData);
            }

            @Override
            public void onDisable() {
                sprite.destroy();
            }

            @Override
            public void processInput() {
                glfwPollEvents();
                handleKeyActions();
            }

            @Override
            public void update() {
                var millis = System.currentTimeMillis();

                if (lastMillis + 300 < millis) {
                    lastMillis = millis;
                    sprite.getActiveSprite().nextFrame();
                }
            }

            @Override
            public void render(double timeShift) {
                renderer.render(List.of(sprite), entityWorldPos);
            }
        };

        gameLoop.enable();
    }

    private void handleKeyActions() {
        float speed = 5;
        if (keys[GLFW_KEY_ESCAPE]) window.close();
        if (keys[GLFW_KEY_W]) entityWorldPos.y -= speed;
        if (keys[GLFW_KEY_S]) entityWorldPos.y += speed;
        if (keys[GLFW_KEY_A]) entityWorldPos.x -= speed;
        if (keys[GLFW_KEY_D]) entityWorldPos.x += speed;

        if (keys[GLFW_KEY_1]) sprite.setActiveSprite(0);
        if (keys[GLFW_KEY_2]) sprite.setActiveSprite(1);
        if (keys[GLFW_KEY_3]) sprite.setActiveSprite(2);
    }
}
