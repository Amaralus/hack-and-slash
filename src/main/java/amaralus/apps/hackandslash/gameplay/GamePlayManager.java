package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.io.KeyEvent;
import amaralus.apps.hackandslash.resources.ResourceManager;
import amaralus.apps.hackandslash.resources.factory.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;

public class GamePlayManager {

    private static final Logger log = LoggerFactory.getLogger(GamePlayManager.class);

    private final boolean[] keys = new boolean[1024];
    private final Window window;
    private final Renderer renderer;

    private Entity testEntity;

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
        testEntity = new Entity(
                new RenderComponent(getService(ResourceFactory.class).produceSprite("testTextureSheet")),
                vec2());
        testEntity.getRenderComponent().startAnimation();

        var testEntity2 = new Entity(
                new RenderComponent(getService(ResourceManager.class).getResource("testTextureSheet", Sprite.class)),
                vec2(150, 100));
        testEntity2.getRenderComponent().setCurrentFrameStrip(2);
        testEntity2.getRenderComponent().startAnimation();

        var inosuke = new Entity(
                new RenderComponent(getService(ResourceFactory.class).produceSprite("inosuke2")),
                vec2());

        var entityList = List.of(testEntity, testEntity2, inosuke);

        var gameLoop = new GameLoop(16L) {

            @Override
            public void onEnable() {
                log.info("Игровой цикл включён");
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
                entityList.forEach(entity -> entity.update(elapsedTime));
            }

            @Override
            public void render(double timeShift) {
                renderer.render(entityList);
            }
        };

        gameLoop.enable();
    }

    private void handleKeyActions() {
        float speed = 5;
        if (keys[GLFW_KEY_ESCAPE]) window.close();

        if (keys[GLFW_KEY_W]) testEntity.moveUp(speed);
        if (keys[GLFW_KEY_S]) testEntity.moveDown(speed);
        if (keys[GLFW_KEY_A]) testEntity.moveLeft(speed);
        if (keys[GLFW_KEY_D]) testEntity.moveRight(speed);

        var renderEntity = testEntity.getRenderComponent();

        if (keys[GLFW_KEY_Q]) renderEntity.setSpriteRotateAngle(renderEntity.getSpriteRotateAngle() - speed);
        if (keys[GLFW_KEY_E]) renderEntity.setSpriteRotateAngle(renderEntity.getSpriteRotateAngle() + speed);

        if (keys[GLFW_KEY_1]) renderEntity.setCurrentFrameStrip(0);
        if (keys[GLFW_KEY_2]) renderEntity.setCurrentFrameStrip(1);
        if (keys[GLFW_KEY_3]) renderEntity.setCurrentFrameStrip(2);
    }
}
