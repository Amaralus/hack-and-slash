package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.gameplay.loop.DefaultGameLoop;
import amaralus.apps.hackandslash.gameplay.loop.GameLoop;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.entities.Color;
import amaralus.apps.hackandslash.graphics.entities.sprites.Animation;
import amaralus.apps.hackandslash.graphics.entities.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.graphics.scene.Scene;
import amaralus.apps.hackandslash.io.events.InputHandler;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.gameplay.CommandsPool.*;
import static amaralus.apps.hackandslash.io.events.KeyCode.*;
import static amaralus.apps.hackandslash.io.events.MouseButton.*;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

@Service
public class GameplayManager {

    @Lazy
    private final Window window;
    private final InputHandler inputHandler;
    private final EntityFactory entityFactory;
    private final ResourceFactory resourceFactory;
    private final UpdateService updateService;
    private final GameLoop gameLoop;

    private final Scene scene;

    private Entity player;
    private Entity triangle;

    public GameplayManager(Window window,
                           InputHandler inputHandler,
                           EntityFactory entityFactory,
                           ResourceFactory resourceFactory,
                           UpdateService updateService,
                           GameLoop gameLoop) {
        this.window = window;
        this.inputHandler = inputHandler;
        this.entityFactory = entityFactory;
        this.resourceFactory = resourceFactory;
        this.updateService = updateService;
        this.gameLoop = gameLoop;

        scene = new Scene(window.getWidth(), window.getHeight());
    }

    public void runGameLoop() {
        setUpInput();
        setUpEntities();

        ((DefaultGameLoop) gameLoop).setScene(scene);

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

    private void setUpEntities() {
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
                vec2(0f, -40f),
                vec2(40f, 40f),
                vec2(-40f, 40f)
        ), vec2());

        var line = new Entity(resourceFactory
                .produceLine("line", Color.CYAN, vec2(-50, -50), vec2(50, 50)),
                vec2());

        updateService.registerEntity(triangle, line, player, entity);
        triangle.addChildren(player, entity);
        scene.addChildren(triangle, line);
    }
}
