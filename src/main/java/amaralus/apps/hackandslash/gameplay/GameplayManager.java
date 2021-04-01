package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.entities.Color;
import amaralus.apps.hackandslash.graphics.entities.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.entities.primitives.Triangle;
import amaralus.apps.hackandslash.graphics.entities.sprites.Animation;
import amaralus.apps.hackandslash.graphics.scene.Scene;
import amaralus.apps.hackandslash.io.events.InputHandler;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static amaralus.apps.hackandslash.gameplay.CommandsPool.*;
import static amaralus.apps.hackandslash.io.events.KeyCode.*;
import static amaralus.apps.hackandslash.io.events.MouseButton.*;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.GLFW.*;

@Service
public class GameplayManager {

    private static final Logger log = LoggerFactory.getLogger(GameplayManager.class);

    @Lazy
    private final Window window;
    private final Renderer renderer;
    private final InputHandler inputHandler;
    private final EntityFactory entityFactory;
    private final ResourceFactory resourceFactory;

    private final Scene scene;

    private Entity player;
    private Primitive primitive;

    public GameplayManager(Window window, Renderer renderer, EntityFactory entityFactory, ResourceFactory resourceFactory) {
        this.window = window;
        this.renderer = renderer;
        scene = new Scene(window.getWidth(), window.getHeight());
        this.entityFactory = entityFactory;
        this.resourceFactory = resourceFactory;
        inputHandler = new InputHandler();
        inputHandler.setUpInputHandling(window);
        setUpInput();
    }

    public void runGameLoop() {
        player = entityFactory.sprite("testTextureSheet")
                .position(0, 0)
                .speed(200)
                .produce();
        player.getRenderComponent().computeAnimation(Animation::start);

        var entity = entityFactory.sprite("testTextureSheet")
                .position(20, 20)
                .speed(200)
                .produce();

        entity.getRenderComponent().changeAnimatedFrameStrip(2);
        entity.getRenderComponent().computeAnimation(Animation::start);
        player.addChildren(entity);

        primitive = resourceFactory.produceTriangle(
                "triangle",
                Color.WHITE,
                vec2(0f, 0.5f),
                vec2(0.5f, -0.5f),
                vec2(-0.5f, -0.5f)
        );

        var entityList = List.of(player, entity);
        scene.addChildren(player);

        var gameLoop = new GameLoop(window, 16L) {

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
                renderer.render(scene);
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
