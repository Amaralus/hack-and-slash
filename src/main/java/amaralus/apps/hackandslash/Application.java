package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.config.ApplicationConfig;
import amaralus.apps.hackandslash.resources.ResourceManager;
import amaralus.apps.hackandslash.gameplay.GameplayManager;
import amaralus.apps.hackandslash.graphics.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import static org.lwjgl.glfw.GLFW.*;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private GenericApplicationContext applicationContext;

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        log.info("Запуск Hack and Slash");

        try (var errorCallback = GLFWErrorCallback.createPrint(System.err).set()) {

            if (!glfwInit()) throw new IllegalStateException("Невозвожно инициализировать GLFW!");

            applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
            applicationContext.getBean(GameplayManager.class).runGameLoop();

            glfwSetErrorCallback(null);

        } catch (Exception e) {
            log.error("Непредвиденная ошибка", e);
            glfwSetErrorCallback(null);
        } finally {
            onShutdown();
        }
    }

    private void onShutdown() {
        log.info("Завершение работы приложения...");
        try {
            var resourceManager = applicationContext.getBean(ResourceManager.class);
            resourceManager.destroy();
        } catch (Exception e) {
            log.error("Непредвиденная ошибка освобождения ресурсов во время завершения", e);
        }

        try {
            var window = applicationContext.getBean(Window.class);
            window.destroy();
        } catch (Exception e) {
            log.error("Непредвиденная ошибка во время закрытия окна", e);
        }

        try {
            applicationContext.close();
        } catch (Exception e) {
            log.error("Непредвиденная ошибка во время завершения контекста spring", e);
        }
        glfwTerminate();
    }
}
