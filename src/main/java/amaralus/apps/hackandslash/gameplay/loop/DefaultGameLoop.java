package amaralus.apps.hackandslash.gameplay.loop;

import amaralus.apps.hackandslash.gameplay.UpdateService;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.rendering.RenderingService;
import amaralus.apps.hackandslash.io.events.InputHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultGameLoop extends GameLoop {

    private final InputHandler inputHandler;
    private final RenderingService renderingService;
    private final UpdateService updateService;

    public DefaultGameLoop(Window window, InputHandler inputHandler, UpdateService updateService, RenderingService renderingService) {
        super(window, 60);
        this.inputHandler = inputHandler;
        this.renderingService = renderingService;
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
        inputHandler.handleInputEvents();
    }

    @Override
    public void update(long elapsedTime) {
        updateService.update(elapsedTime);
    }

    @Override
    public void render(double timeShift) {
        renderingService.render();
    }
}
