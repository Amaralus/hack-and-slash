package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityFactory;
import amaralus.apps.hackandslash.gameplay.loop.GameLoop;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.rendering.RendererService;
import amaralus.apps.hackandslash.graphics.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.io.events.InputHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.io.events.keyboard.KeyCode.*;
import static amaralus.apps.hackandslash.io.events.mouse.MouseButton.MOUSE_BUTTON_LEFT;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameplayManager {

    private final Window window;
    private final InputHandler inputHandler;
    private final EntityFactory entityFactory;
    private final GameLoop gameLoop;
    private final RendererService rendererService;

    private int currentFrame = 0;

    public void runGameLoop() {
        setUpInput();

        gameLoop.enable();
    }

    private void setUpInput() {
        inputHandler.singleAction(ESCAPE, window::close)
                .singleAction(DIG1, () -> setCurrentFrame(0))
                .singleAction(DIG2, () -> setCurrentFrame(1))
                .singleAction(DIG3, () -> setCurrentFrame(2))
                .singleAction(DIG4, () -> setCurrentFrame(3))
                .singleAction(DIG5, () -> setCurrentFrame(4))
                .singleAction(MOUSE_BUTTON_LEFT, this::spawnBot);
    }

    private void spawnBot() {
        var bot = entityFactory.entity()
                .renderComponent(entityFactory.spriteRenderComponent()
                        .spriteName("stoneScissorsPaperLizardSpock")
                        .produce())
                .position(rendererService.getGlobalCursorPosition())
                .movementSpeed(200)
                .register();

        changeFrame(bot, currentFrame);
    }

    private void changeFrame(Entity entity, int frame) {
        entity.getRenderComponent().wrapTo(SpriteRenderComponent.class).getCurrentFramesStripContext().setCurrentFrame(frame);
    }

    private void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
}
