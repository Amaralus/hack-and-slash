package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.common.ApplicationLoader;
import amaralus.apps.hackandslash.resources.ResourceManager;
import amaralus.apps.hackandslash.gameplay.GameplayManager;
import amaralus.apps.hackandslash.graphics.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static org.lwjgl.glfw.GLFW.*;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        log.info("Запуск Hack and Slash");

        try (var errorCallback = GLFWErrorCallback.createPrint(System.err).set()) {

            if (!glfwInit()) throw new IllegalStateException("Невозвожно инициализировать GLFW!");

            Window.create(800, 600, "Hack and Slash").show();

            new ApplicationLoader().initLoading();
            getService(GameplayManager.class).runGameLoop();

            glfwSetErrorCallback(null);

        } catch (Exception e) {
            log.error("Непредвиденная ошибка", e);
            glfwSetErrorCallback(null);
        } finally {
            onShutdown();
        }
    }

    private void onShutdown() {
        var resourceManager = getService(ResourceManager.class);
        if (resourceManager != null) resourceManager.destroy();

        var window = getService(Window.class);
        if (window != null) window.destroy();

        glfwTerminate();
    }
}
