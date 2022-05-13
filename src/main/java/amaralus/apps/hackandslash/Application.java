package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.common.ApplicationLoader;
import amaralus.apps.hackandslash.config.ApplicationConfig;
import amaralus.apps.hackandslash.gameplay.GameplayManager;
import amaralus.apps.hackandslash.graphics.Window;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class Application {

    private GenericApplicationContext applicationContext;

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        log.info("Запуск Hack and Slash");

        try (var ignored = GLFWErrorCallback.createPrint(System.err).set()) {

            if (!glfwInit()) throw new IllegalStateException("Невозможно инициализировать GLFW!");

            applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
            applicationContext.getBean(ApplicationLoader.class).initLoading();
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
            var window = applicationContext.getBean(Window.class);
            window.destroy();
        } catch (Exception e) {
            log.error("Непредвиденная ошибка во время закрытия окна", e);
        }

        try {
            applicationContext.close();
        } catch (Exception e) {
            log.error("Непредвиденная ошибка во время завершения spring-контекста", e);
        }
        glfwTerminate();
    }
}
