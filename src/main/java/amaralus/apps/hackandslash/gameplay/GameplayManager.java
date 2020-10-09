package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.entities.Color;
import amaralus.apps.hackandslash.graphics.entities.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.entities.primitives.Triangle;
import amaralus.apps.hackandslash.graphics.entities.sprites.Animation;
import amaralus.apps.hackandslash.io.events.InputHandler;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static amaralus.apps.hackandslash.gameplay.CommandsPool.*;
import static amaralus.apps.hackandslash.io.events.KeyCode.*;
import static amaralus.apps.hackandslash.io.events.MouseButton.*;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;

public class GameplayManager {

    private static final Logger log = LoggerFactory.getLogger(GameplayManager.class);

    private final Window window;
    private final Renderer renderer;
    private final InputHandler inputHandler;

    private Entity player;
    private Primitive primitive;

    public GameplayManager(Window window) {
        this.window = window;
        renderer = new Renderer(window);
        inputHandler = new InputHandler();
        inputHandler.setUpInputHandling(window);
        setUpInput();
    }

    public void runGameLoop() {
        player = new EntityFactory()
                .sprite("testTextureSheet")
                .position(0, 0)
                .speed(200)
                .produce();
        player.getRenderComponent().computeAnimation(Animation::start);

        primitive = getService(ResourceFactory.class).produceTriangle(
                "triangle",
                Color.YELLOW,
                vec2(0f, 0.5f),
                vec2(0.5f, -0.5f),
                vec2(-0.5f, -0.5f)
        );

        var entityList = List.of(player);

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
                inputHandler.executeActions();
            }

            @Override
            public void update(long elapsedTime) {
                entityList.forEach(entity -> entity.update(elapsedTime));
            }

            @Override
            public void render(double timeShift) {
                renderer.render(entityList, List.of(primitive));
            }
        };

        gameLoop.enable();
    }

    private void setUpInput() {
        inputHandler.addAction(ESCAPE, window::close);

        inputHandler.addAction(W, () -> player.getInputComponent().addCommand(ENTITY_MOVE_UP));
        inputHandler.addAction(S, () -> player.getInputComponent().addCommand(ENTITY_MOVE_DOWN));
        inputHandler.addAction(A, () -> player.getInputComponent().addCommand(ENTITY_MOVE_LEFT));
        inputHandler.addAction(D, () -> player.getInputComponent().addCommand(ENTITY_MOVE_RIGHT));

        inputHandler.addAction(Q, () -> player.getRenderComponent().addSpriteRotateAngle(-5f));
        inputHandler.addAction(E, () -> player.getRenderComponent().addSpriteRotateAngle(5f));

        inputHandler.addAction(DIG1, () -> player.getRenderComponent().changeAnimatedFrameStrip(0));
        inputHandler.addAction(DIG2, () -> player.getRenderComponent().changeAnimatedFrameStrip(1));
        inputHandler.addAction(DIG3, () -> player.getRenderComponent().changeAnimatedFrameStrip(2));

        inputHandler.addAction(MOUSE_BUTTON_LEFT, () -> player.setPosition(
                renderer.getCamera().getWordPosOfScreenPos(window.getCursorPosition())));

        inputHandler.addAction(MOUSE_BUTTON_RIGHT, () -> ((Triangle) primitive).updateFirst(window.windowPosToGlPos(window.getCursorPosition())));

        inputHandler.setScrollAction((xOfsset, yOffset) -> renderer.getCamera().addScale(yOffset));
    }
}
