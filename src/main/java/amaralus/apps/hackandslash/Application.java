package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.graphics.OrthoCamera;
import amaralus.apps.hackandslash.graphics.SpriteRenderer;
import amaralus.apps.hackandslash.graphics.Texture;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.*;

import static amaralus.apps.hackandslash.VectMatrUtil.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private float width = 800;
    private float height = 600;

    private Vector2f entityWorldPos = vec2(0f, -0f);
    private OrthoCamera camera;

    private boolean[] keys = new boolean[1024];

    private long windowHandle;

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        log.info("Запуск Hack and Slash");

        try (var errorCallback = GLFWErrorCallback.createPrint(System.err).set()) {
            init();
            loop();
            glfwSetErrorCallback(null);

        } catch (Exception e) {
            log.error("Непредвиденная ошибка", e);
            glfwSetErrorCallback(null);
        } finally {
            glfwFreeCallbacks(windowHandle);
            glfwDestroyWindow(windowHandle);
            glfwTerminate();
        }
    }

    private void init() {
        if (!glfwInit()) throw new IllegalStateException("Невозвожно инициализировать GLFW!");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        windowHandle = glfwCreateWindow((int) width, (int) height, "Hack and Slash", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Ошибка создания GLFW окна");

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS)
                keys[key] = true;
            if (action == GLFW_RELEASE)
                keys[key] = false;
        });

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

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
    }

    private void loop() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        camera = new OrthoCamera(width, height, vec2(0f, 0f));
        camera.setScale(4.5f);
        var spriteRenderer = new SpriteRenderer();

        var texture = new Texture("inosuke2");
        var texture2 = new Texture("illumicati2");

        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();
            handleKeyActions();

            glClearColor(0f,0f,0f,1f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            spriteRenderer.draw(camera, texture, entityWorldPos, 0f);
            spriteRenderer.draw(camera, texture2, vec2(0f, 0f), 0f);

            glfwSwapBuffers(windowHandle);
        }

        spriteRenderer.destroy();
    }

    private void handleKeyActions() {
        float speed = 5;
        if (keys[GLFW_KEY_ESCAPE]) glfwSetWindowShouldClose(windowHandle, true);

        if (keys[GLFW_KEY_W]) entityWorldPos.y -= speed;
        if (keys[GLFW_KEY_S]) entityWorldPos.y += speed;
        if (keys[GLFW_KEY_A]) entityWorldPos.x -= speed;
        if (keys[GLFW_KEY_D]) entityWorldPos.x += speed;

        if(keys[GLFW_KEY_I]) camera.moveUp(speed);
        if(keys[GLFW_KEY_K]) camera.moveDown(speed);
        if(keys[GLFW_KEY_J]) camera.moveLeft(speed);
        if(keys[GLFW_KEY_L]) camera.moveRight(speed);
    }
}
