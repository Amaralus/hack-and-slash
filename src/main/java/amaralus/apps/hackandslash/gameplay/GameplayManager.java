package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.entities.Color;
import amaralus.apps.hackandslash.graphics.entities.sprites.Animation;
import amaralus.apps.hackandslash.graphics.entities.sprites.SpriteRenderComponent;
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
    private Entity triangle;

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
        player.getRenderComponent().wrapTo(SpriteRenderComponent.class).computeAnimation(Animation::start);

        var entity = entityFactory.sprite("testTextureSheet")
                .position(20, 20)
                .speed(200)
                .produce();

        entity.getRenderComponent().wrapTo(SpriteRenderComponent.class).changeAnimatedFrameStrip(2);
        entity.getRenderComponent().wrapTo(SpriteRenderComponent.class).computeAnimation(Animation::start);

        triangle = new Entity(resourceFactory.produceTriangle(
                "triangle",
                Color.WHITE,
                vec2(0f, -20f),
                vec2(10f, -10f),
                vec2(-10f, -10f)
        ), vec2());

        var line = new Entity(resourceFactory
                .produceLine("line", Color.CYAN, vec2(-50, -50), vec2(50, 50)),
                vec2());

        var entityList = List.of(triangle, player, entity);
        triangle.addChildren(player, entity);
        scene.addChildren(triangle, line);

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

        inputHandler.addAction(Q, () -> player.getRenderComponent().wrapTo(SpriteRenderComponent.class).addSpriteRotateAngle(-5f));
        inputHandler.addAction(E, () -> player.getRenderComponent().wrapTo(SpriteRenderComponent.class).addSpriteRotateAngle(5f));

        inputHandler.addAction(DIG1, () -> player.getRenderComponent().wrapTo(SpriteRenderComponent.class).changeAnimatedFrameStrip(0));
        inputHandler.addAction(DIG2, () -> player.getRenderComponent().wrapTo(SpriteRenderComponent.class).changeAnimatedFrameStrip(1));
        inputHandler.addAction(DIG3, () -> player.getRenderComponent().wrapTo(SpriteRenderComponent.class).changeAnimatedFrameStrip(2));

        inputHandler.addAction(MOUSE_BUTTON_LEFT, () -> player.setPosition(
                scene.getCamera().getWordPosOfScreenPos(window.getCursorPosition())));

        inputHandler.addAction(MOUSE_BUTTON_RIGHT, () -> triangle.setPosition(scene.getCamera().getWordPosOfScreenPos(window.getCursorPosition())));

        inputHandler.setScrollAction((xOfsset, yOffset) -> scene.getCamera().addScale(yOffset));
    }
}
