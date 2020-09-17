package amaralus.apps.hackandslash.services;

import amaralus.apps.hackandslash.graphics.RenderEntity;
import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.io.KeyEvent;
import amaralus.apps.hackandslash.resources.factory.ResourceFactory;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;

public class GamePlayManager {

    private static final Logger log = LoggerFactory.getLogger(GamePlayManager.class);
    private static final String SPRITE_NAME = "testTextureSheet";

    private final boolean[] keys = new boolean[1024];
    private final Window window;
    private final Renderer renderer;

    private RenderEntity renderEntity;

    private final Vector2f entityWorldPos = vec2(0f, 0f);

    public GamePlayManager(Window window) {
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
        renderEntity = new RenderEntity(getService(ResourceFactory.class).produceSpriteSheet(SPRITE_NAME));

        var gameLoop = new GameLoop(16L) {

            @Override
            public void onEnable() {
                log.info("Игровой цикл включён");
                renderEntity.startAnimation();
            }

            @Override
            public void onDisable() {
                log.info("Игровой цикл отключён");
            }

            @Override
            public void processInput() {
                glfwPollEvents();
                handleKeyActions();
            }

            @Override
            public void update(long elapsedTime) {
                renderEntity.update(elapsedTime);
            }

            @Override
            public void render(double timeShift) {
                renderer.render(List.of(renderEntity), entityWorldPos);
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
        if (keys[GLFW_KEY_Q]) renderEntity.setSpriteRotateAngle(renderEntity.getSpriteRotateAngle() - speed);
        if (keys[GLFW_KEY_E]) renderEntity.setSpriteRotateAngle(renderEntity.getSpriteRotateAngle() + speed);

        if (keys[GLFW_KEY_1]) renderEntity.setCurrentFrameStrip(0);
        if (keys[GLFW_KEY_2]) renderEntity.setCurrentFrameStrip(1);
        if (keys[GLFW_KEY_3]) renderEntity.setCurrentFrameStrip(2);
    }
}
