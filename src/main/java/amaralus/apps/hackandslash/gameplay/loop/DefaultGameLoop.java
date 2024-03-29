package amaralus.apps.hackandslash.gameplay.loop;

import amaralus.apps.hackandslash.gameplay.UpdateService;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.rendering.RendererService;
import amaralus.apps.hackandslash.io.events.InputHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultGameLoop extends GameLoop {

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
        inputHandler.handleInputEvents();
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
