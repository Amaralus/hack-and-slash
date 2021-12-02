package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.io.events.keyboard.KeyboardKeyEvent;
import amaralus.apps.hackandslash.io.events.mouse.MouseButtonEvent;
import amaralus.apps.hackandslash.io.events.mouse.ScrollEvent;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.function.Consumer;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.opengl.GL11.*;
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

        enableOpenGLProperties();
    }

    private void enableOpenGLProperties() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
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

    public void setKeyCallback(Consumer<KeyboardKeyEvent> keyEventConsumer) {
        glfwSetKeyCallback(windowHandle, (eventWindowHandle, key, scancode, action, mods) ->
                keyEventConsumer.accept(new KeyboardKeyEvent(eventWindowHandle, key, scancode, action, mods)));
    }

    public void setMouseButtonCallback(Consumer<MouseButtonEvent> buttonEventConsumer) {
        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) ->
                buttonEventConsumer.accept(new MouseButtonEvent(window, button, action, mods)));
    }

    public void setScrollCallback(Consumer<ScrollEvent> scrollEventConsumer) {
        glfwSetScrollCallback(windowHandle, (window, xOffset, yOffset) ->
                scrollEventConsumer.accept(new ScrollEvent(window, xOffset, yOffset)));
    }

    public Vector2f getCursorPosition() {
        var xBuffer = BufferUtils.createDoubleBuffer(1);
        var yBuffer = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(windowHandle, xBuffer, yBuffer);

        return vec2((float) xBuffer.get(0), (float) yBuffer.get(0));
    }

    public Vector2f windowPosToGlPos(Vector2f position) {
        float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;

        return position.sub(halfWidth, halfHeight).div(halfWidth, -halfHeight);
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
