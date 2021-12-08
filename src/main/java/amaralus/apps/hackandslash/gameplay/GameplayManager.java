package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityFactory;
import amaralus.apps.hackandslash.gameplay.loop.GameLoop;
import amaralus.apps.hackandslash.gameplay.state.StateFactory;
import amaralus.apps.hackandslash.gameplay.state.action.InputEventProcessingAction;
import amaralus.apps.hackandslash.gameplay.state.action.MessageProcessingStateAction;
import amaralus.apps.hackandslash.graphics.RendererService;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.io.events.InputEventMessage;
import amaralus.apps.hackandslash.io.events.InputHandler;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.gameplay.CommandsPool.*;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.SLEEPING;
import static amaralus.apps.hackandslash.gameplay.message.SystemTopic.INPUT_TOPIC;
import static amaralus.apps.hackandslash.graphics.entities.Color.CYAN;
import static amaralus.apps.hackandslash.graphics.entities.Color.WHITE;
import static amaralus.apps.hackandslash.graphics.scene.NodeRemovingStrategy.CASCADE;
import static amaralus.apps.hackandslash.io.events.keyboard.KeyCode.*;
import static amaralus.apps.hackandslash.io.events.mouse.MouseButton.MOUSE_BUTTON_LEFT;
import static amaralus.apps.hackandslash.io.events.mouse.MouseButton.MOUSE_BUTTON_RIGHT;
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
        inputHandler.singleAction(ESCAPE, window::close)
                .buttonAction(W)
                .buttonAction(S)
                .buttonAction(A)
                .buttonAction(D)
                .singleAction(DIG1)
                .singleAction(DIG2)
                .singleAction(DIG3)
                .singleAction(R)
                .singleAction(MOUSE_BUTTON_LEFT)
                .singleAction(MOUSE_BUTTON_RIGHT, () -> triangle.getPhysicalComponent().setPosition(rendererService.getGlobalCursorPosition()))
                .scrollAction((xOffset, yOffset) -> rendererService.getActiveScene().getCamera().addScale(yOffset));
    }

    private void setUpEntities() {

        setUpTriangle();

        setUpPlayer();

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

    private void setUpPlayer() {
        player = entityFactory.entity()
                .renderComponent(entityFactory.spriteRenderComponent()
                        .spriteName("testTextureSheet")
                        .runAnimation()
                        .produce())
                .movementSpeed(200)
                .targetNode(triangle)
                .register();

        player.getMessageClient().subscribe(INPUT_TOPIC);

        player.setStateSystem(new StateFactory()
                .baseState("base", new MessageProcessingStateAction()
                        .onMessage(InputEventMessage.class, new InputEventProcessingAction()
                                .onButton(W, (stateContext, updateTime) -> ENTITY_MOVE_UP.execute(stateContext.entity()))
                                .onButton(S, (stateContext, updateTime) -> ENTITY_MOVE_DOWN.execute(stateContext.entity()))
                                .onButton(A, (stateContext, updateTime) -> ENTITY_MOVE_LEFT.execute(stateContext.entity()))
                                .onButton(D, (stateContext, updateTime) -> ENTITY_MOVE_RIGHT.execute(stateContext.entity()))
                                .onButton(DIG1, (stateContext, updateTime) -> changeFrameStrip(0).execute(stateContext.entity()))
                                .onButton(DIG2, (stateContext, updateTime) -> changeFrameStrip(1).execute(stateContext.entity()))
                                .onButton(DIG3, (stateContext, updateTime) -> changeFrameStrip(2).execute(stateContext.entity()))
                                .onButton(MOUSE_BUTTON_LEFT, (stateContext, updateTime) ->
                                        stateContext.entity().getPhysicalComponent().setPosition(rendererService.getGlobalCursorPosition()))
                        ))
                .produce());
    }

    private void setUpTriangle() {
        triangle = entityFactory.entity()
                .renderComponent(entityFactory.primitiveRenderComponent()
                        .triangle(vec2(0f, -40f), vec2(40f, 40f), vec2(-40f, 40f))
                        .primitiveName("triangle")
                        .color(WHITE)
                        .produce())
                .removingStrategy(CASCADE)
                .register();

        triangle.getMessageClient().subscribe(INPUT_TOPIC);

        triangle.setStateSystem(new StateFactory()
                .baseState("base", new MessageProcessingStateAction()
                        .onMessage(InputEventMessage.class, new InputEventProcessingAction()
                                .onButton(R, (stateContext, updateTime) -> stateContext.entity().setStatus(REMOVE))
                                .onButton(MOUSE_BUTTON_RIGHT, (stateContext, updateTime) ->
                                        stateContext.entity().getPhysicalComponent().setPosition(rendererService.getGlobalCursorPosition()))
                        ))
                .produce());
    }
}
