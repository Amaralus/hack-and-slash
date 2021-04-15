package amaralus.apps.hackandslash.gameplay.loop;

import amaralus.apps.hackandslash.gameplay.UpdateService;
import amaralus.apps.hackandslash.graphics.RendererService;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.io.events.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

@Component
public class DefaultGameLoop extends GameLoop {

    private static final Logger log = LoggerFactory.getLogger(DefaultGameLoop.class);

    private final InputHandler inputHandler;
    private final RendererService rendererService;
    private final UpdateService updateService;

    public DefaultGameLoop(Window window, InputHandler inputHandler, UpdateService updateService, RendererService rendererService) {
        super(window, 60);
        this.inputHandler = inputHandler;
        this.rendererService = rendererService;
        this.updateService = updateService;
    }

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
        updateService.update(elapsedTime);
    }

    @Override
    public void render(double timeShift) {
        rendererService.render();
    }
}
