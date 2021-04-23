package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityFactory;
import amaralus.apps.hackandslash.gameplay.loop.GameLoop;
import amaralus.apps.hackandslash.graphics.RendererService;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.entities.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.io.events.InputHandler;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.gameplay.CommandsPool.*;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.*;
import static amaralus.apps.hackandslash.gameplay.entity.RemovingStrategy.CASCADE;
import static amaralus.apps.hackandslash.graphics.entities.Color.CYAN;
import static amaralus.apps.hackandslash.graphics.entities.Color.WHITE;
import static amaralus.apps.hackandslash.io.events.KeyCode.*;
import static amaralus.apps.hackandslash.io.events.MouseButton.*;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

@Service
public class GameplayManager {

    private final Window window;
    private final InputHandler inputHandler;
    private final EntityFactory entityFactory;
    private final GameLoop gameLoop;
    private final RendererService rendererService;

    private Entity player;
    private Entity triangle;

    public GameplayManager(Window window,
                           InputHandler inputHandler,
                           EntityFactory entityFactory,
                           GameLoop gameLoop,
                           RendererService rendererService) {
        this.window = window;
        this.inputHandler = inputHandler;
        this.entityFactory = entityFactory;
        this.gameLoop = gameLoop;

        this.rendererService = rendererService;
    }

    public void runGameLoop() {
        setUpInput();
        setUpEntities();

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

        inputHandler.addAction(R, () -> triangle.setStatus(REMOVE));

        inputHandler.addAction(MOUSE_BUTTON_LEFT, () -> player.setPosition(
                rendererService.getActiveScene().getCamera().getWordPosOfScreenPos(window.getCursorPosition())));

        inputHandler.addAction(MOUSE_BUTTON_RIGHT, () -> triangle.setPosition(
                rendererService.getActiveScene().getCamera().getWordPosOfScreenPos(window.getCursorPosition())));

        inputHandler.setScrollAction((xOfsset, yOffset) -> rendererService.getActiveScene().getCamera().addScale(yOffset));
    }

    private void setUpEntities() {

        triangle = entityFactory.entity()
                .renderComponent(entityFactory.primitiveRenderComponent()
                        .triangle(vec2(0f, -40f), vec2(40f, 40f), vec2(-40f, 40f))
                        .primitiveName("triangle")
                        .color(WHITE)
                        .produce())
                .removingStrategy(CASCADE)
                .register();

        player = entityFactory.entity()
                .renderComponent(entityFactory.spriteRenderComponent()
                        .spriteName("testTextureSheet")
                        .runAnimation()
                        .produce())
                .movementSpeed(200)
                .targetNode(triangle)
                .register();

        entityFactory.entity()
                .renderComponent(entityFactory.spriteRenderComponent()
                        .spriteName("testTextureSheet")
                        .frameStrip(2)
                        .runAnimation()
                        .produce())
                .position(20, 20)
                .movementSpeed(200)
                .targetNode(triangle)
                .register();

        entityFactory.entity()
                .renderComponent(entityFactory.primitiveRenderComponent()
                        .line(vec2(-50, -50), vec2(50, 50))
                        .primitiveName("line")
                        .color(CYAN)
                        .produce())
                .entityStatus(SLEEPING)
                .register();
    }
}
