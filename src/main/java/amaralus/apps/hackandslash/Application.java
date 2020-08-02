package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.graphics.Camera;
import amaralus.apps.hackandslash.graphics.Shader;
import amaralus.apps.hackandslash.graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.*;

import static org.joml.Math.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private float width = 800;
    private float height = 600;

    Camera camera = new Camera(width, height, 0f, 0f, 3);

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

        var shader = new Shader("vertex", "fragment");

        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();

        setUpVertexData(vao, vbo, ebo);
        var texture = new Texture("inosuke2");
        var projection = new Matrix4f().ortho(0.0f, width, height, 0.0f, -1.0f, 1.0f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();
            handleKeyActions();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.use();

            var position = new Vector2f(200.0f, 200.0f);
            var size = new Vector2f(300.0f, 400.0f);

            var model = new Matrix4f()
                    .translate(new Vector3f(position, 1f))
                    .translate(new Vector3f(0.5f * size.x, 0.5f * size.y, 0f))
                    .rotate(toRadians(45f), new Vector3f(0f, 0f, 1f))
                    .translate(new Vector3f(-0.5f * size.x, -0.5f * size.y, 0f))
                    .scale(new Vector3f(size, 1f));

            shader.setMatrix4("model", model);
            shader.setMatrix4("projection", projection);

            texture.bind();

            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, 6);
            glBindVertexArray(0);

            glfwSwapBuffers(windowHandle);
        }

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }

    private void handleKeyActions() {
        if (keys[GLFW_KEY_ESCAPE]) glfwSetWindowShouldClose(windowHandle, true);
    }

    private float[] vertices() {
        return new float[]{
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };
    }

    private int[] indices() {
        return new int[]{
                0, 1, 3,   // Первый треугольник
                1, 2, 3    // Второй треугольник
        };
    }

    private void setUpVertexData(int vao, int vbo, int ebo) {
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices(), GL_STATIC_DRAW);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
