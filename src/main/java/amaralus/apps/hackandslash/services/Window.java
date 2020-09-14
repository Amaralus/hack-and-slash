package amaralus.apps.hackandslash.services;

import amaralus.apps.hackandslash.Destroyable;
import amaralus.apps.hackandslash.io.KeyEvent;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.function.Consumer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Destroyable {

    private final int width;
    private final int height;
    private final long windowHandle;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL)
            throw new WindowCreationException("Ошибка создания окна!");

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
    }

    public void show() {
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
    }

    public void close() {
        glfwSetWindowShouldClose(windowHandle, true);
    }

    public boolean isShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void swapBuffers() {
        glfwSwapBuffers(windowHandle);
    }

    public void setKeyCallBack(Consumer<KeyEvent> keyEventConsumer) {
        glfwSetKeyCallback(windowHandle, (eventWindowHandle, key, scancode, action, mods) ->
                keyEventConsumer.accept(new KeyEvent(eventWindowHandle, key, scancode, action, mods)));
    }

    @Override
    public void destroy() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public static class WindowCreationException extends RuntimeException {

        public WindowCreationException(String message) {
            super(message);
        }
    }
}
