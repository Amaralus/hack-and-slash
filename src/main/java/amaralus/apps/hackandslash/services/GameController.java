package amaralus.apps.hackandslash.services;

import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.data.sprites.SpriteSheet;
import amaralus.apps.hackandslash.io.KeyEvent;
import amaralus.apps.hackandslash.resources.factory.ResourceFactory;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;

public class GameController {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);
    private static final String SPRITE_NAME = "testTextureSheet";

    private final boolean[] keys = new boolean[1024];
    private final Window window;
    private final Renderer renderer;

    private final Vector2f entityWorldPos = vec2(0f, 0f);
    private long lastMillis = System.currentTimeMillis();

    public GameController(Window window) {
        this.window = window;
        getService(ResourceFactory.class).produceShader("texture");
        renderer = new Renderer(window);
        window.setKeyCallBack(this::updateKeyEvent);
    }

    public void updateKeyEvent(KeyEvent event) {
        if (GLFW_PRESS == event.getAction())
            keys[event.getKey()] = true;
        if (GLFW_RELEASE == event.getAction())
            keys[event.getKey()] = false;
    }

    public void runGameLoop() {
        getService(ResourceFactory.class).produceEbo("defaultTexture", new int[]{0, 1, 3, 1, 2, 3});
        getService(ResourceFactory.class).produceSpriteSheet(SPRITE_NAME);

        var gameLoop = new GameLoop(10L) {

            @Override
            public void onEnable() {
                log.debug("Игровой цикл включён");
            }

            @Override
            public void onDisable() {
                log.debug("Игровой цикл отключён");
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
                    getService(ResourceManager.class)
                            .getResource(SPRITE_NAME, SpriteSheet.class)
                            .getActiveSprite().nextFrame();
                }
            }

            @Override
            public void render(double timeShift) {
                renderer.render(List.of(getService(ResourceManager.class).getResource(SPRITE_NAME, SpriteSheet.class)), entityWorldPos);
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

        var sprite = getService(ResourceManager.class).getResource(SPRITE_NAME, SpriteSheet.class);
        if (keys[GLFW_KEY_1]) sprite.setActiveSprite(0);
        if (keys[GLFW_KEY_2]) sprite.setActiveSprite(1);
        if (keys[GLFW_KEY_3]) sprite.setActiveSprite(2);
    }
}
