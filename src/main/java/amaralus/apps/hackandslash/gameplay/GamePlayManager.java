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
    }

    public void runGameLoop() {
        testEntity = new Entity(
                new RenderComponent(getService(ResourceFactory.class).produceSprite("testTextureSheet")),
                vec2());
        testEntity.getRenderComponent().computeAnimation(Animation::start);

        var testEntity2 = new Entity(
                new RenderComponent(getService(ResourceManager.class).getResource("testTextureSheet", Sprite.class)),
                vec2(150, 100));
        testEntity2.getRenderComponent().setCurrentFrameStrip(2);
        testEntity2.getRenderComponent().computeAnimation(Animation::start);

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
        if (inputHandler.isPressed(ESCAPE)) window.close();

        if (inputHandler.isPressed(W)) testEntity.moveUp(speed);
        if (inputHandler.isPressed(S)) testEntity.moveDown(speed);
        if (inputHandler.isPressed(A)) testEntity.moveLeft(speed);
        if (inputHandler.isPressed(D)) testEntity.moveRight(speed);

        var renderComponent = testEntity.getRenderComponent();

        if (inputHandler.isPressed(Q)) renderComponent.setSpriteRotateAngle(renderComponent.getSpriteRotateAngle() - speed);
        if (inputHandler.isPressed(E)) renderComponent.setSpriteRotateAngle(renderComponent.getSpriteRotateAngle() + speed);

        if (inputHandler.isPressed(DIG1) || inputHandler.isPressed(DIG2) || inputHandler.isPressed(DIG3)) {
            renderComponent.computeAnimation(Animation::stopAndReset);
            if (inputHandler.isPressed(DIG1)) renderComponent.setCurrentFrameStrip(0);
            if (inputHandler.isPressed(DIG2)) renderComponent.setCurrentFrameStrip(1);
            if (inputHandler.isPressed(DIG3)) renderComponent.setCurrentFrameStrip(2);
            renderComponent.computeAnimation(Animation::start);
        }
    }
}
