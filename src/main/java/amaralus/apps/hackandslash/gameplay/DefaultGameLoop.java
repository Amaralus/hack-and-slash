package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.Renderer;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.scene.Scene;
import amaralus.apps.hackandslash.io.events.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class DefaultGameLoop extends GameLoop {

    private static final Logger log = LoggerFactory.getLogger(DefaultGameLoop.class);

    private final InputHandler inputHandler;
    private final Renderer renderer;
    private final UpdateService updateService;

    private Scene scene;

    public DefaultGameLoop(Window window, InputHandler inputHandler, UpdateService updateService, Renderer renderer) {
        super(window, 60);
        this.inputHandler = inputHandler;
        this.renderer = renderer;
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
        renderer.render(scene);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
