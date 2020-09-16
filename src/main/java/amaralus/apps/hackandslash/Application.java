package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.resources.factory.ResourceFactory;
import amaralus.apps.hackandslash.resources.ResourceManager;
import amaralus.apps.hackandslash.services.GamePlayManager;
import amaralus.apps.hackandslash.services.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;
import static amaralus.apps.hackandslash.services.ServiceLocator.registerService;
import static org.lwjgl.glfw.GLFW.*;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private Window window;

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        log.info("Запуск Hack and Slash");

        try (var errorCallback = GLFWErrorCallback.createPrint(System.err).set()) {

            if (!glfwInit()) throw new IllegalStateException("Невозвожно инициализировать GLFW!");

            window = new Window(800, 600, "Hack and Slash");
            registerService(window);
            window.show();

            loadServices();
            getService(GamePlayManager.class).runGameLoop();

            glfwSetErrorCallback(null);

        } catch (Exception e) {
            log.error("Непредвиденная ошибка", e);
            glfwSetErrorCallback(null);
        } finally {
            shutdown();
        }
    }

    private void shutdown() {
        var resourceManager = getService(ResourceManager.class);
        if (resourceManager != null) resourceManager.destroy();

        if (window != null) {
            window.destroy();
        }

        glfwTerminate();
    }

    private void loadServices() {
        registerService(new FileLoadService());
        registerService(new ResourceManager());
        registerService(new ResourceFactory(getService(ResourceManager.class)));
        registerService(new GamePlayManager(window));
    }
}
