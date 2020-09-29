package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.graphics.entities.sprites.Animation;
import amaralus.apps.hackandslash.io.InputHandler;
import amaralus.apps.hackandslash.resources.ResourceManager;
import amaralus.apps.hackandslash.resources.factory.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static amaralus.apps.hackandslash.gameplay.CommandsPool.*;
import static amaralus.apps.hackandslash.io.entities.KeyCode.*;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;

public class GamePlayManager {

    private static final Logger log = LoggerFactory.getLogger(GamePlayManager.class);

    private final Window window;
    private final Renderer renderer;
    private final InputHandler inputHandler;

    private Entity testEntity;

    public GamePlayManager(Window window) {
        this.window = window;
        getService(ResourceFactory.class).produceShader("texture");
        renderer = new Renderer(window);
        inputHandler = new InputHandler();
        window.setKeyCallBack(inputHandler::handleKeyEvents);
        setUpInput();
    }

    public void runGameLoop() {
        float speed = 200;
        testEntity = new Entity(
                new RenderComponent(getService(ResourceFactory.class).produceSprite("testTextureSheet")),
                vec2(),
                speed);
        testEntity.getRenderComponent().computeAnimation(Animation::start);

        var testEntity2 = new Entity(
                new RenderComponent(getService(ResourceManager.class).getResource("testTextureSheet", Sprite.class)),
                vec2(150, 100),
                speed);
        testEntity2.getRenderComponent().setCurrentFrameStrip(2);
        testEntity2.getRenderComponent().computeAnimation(Animation::start);

        var inosuke = new Entity(
                new RenderComponent(getService(ResourceFactory.class).produceSprite("inosuke2")),
                vec2(),
                speed);

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
                inputHandler.executeKeyActions();
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

    private void setUpInput() {
        inputHandler.addAction(ESCAPE, window::close);

        inputHandler.addAction(W, () -> testEntity.getInputComponent().addCommand(ENTITY_MOVE_UP));
        inputHandler.addAction(S, () -> testEntity.getInputComponent().addCommand(ENTITY_MOVE_DOWN));
        inputHandler.addAction(A, () -> testEntity.getInputComponent().addCommand(ENTITY_MOVE_LEFT));
        inputHandler.addAction(D, () -> testEntity.getInputComponent().addCommand(ENTITY_MOVE_RIGHT));

        inputHandler.addAction(Q, () -> testEntity.getRenderComponent().addSpriteRotateAngle(-5f));
        inputHandler.addAction(E, () -> testEntity.getRenderComponent().addSpriteRotateAngle(5f));
        inputHandler.addAction(EQUAL, () -> renderer.getCamera().addScale(-0.05f));
        inputHandler.addAction(MINUS, () -> renderer.getCamera().addScale(0.05f));

        inputHandler.addAction(DIG1, () -> testEntity.getRenderComponent().changeAnimatedFrameStrip(0));
        inputHandler.addAction(DIG2, () -> testEntity.getRenderComponent().changeAnimatedFrameStrip(1));
        inputHandler.addAction(DIG3, () -> testEntity.getRenderComponent().changeAnimatedFrameStrip(2));
    }
}
