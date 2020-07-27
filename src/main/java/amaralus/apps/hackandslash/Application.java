package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.io.FileLoadService;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.*;

import static org.joml.Math.toRadians;
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

    private long windowHandle;
    private FileLoadService fileLoadService = new FileLoadService();

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
            glfwSetErrorCallback(null);
            log.error("Непредвиденная ошибка", e);
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

        windowHandle = glfwCreateWindow(800, 600, "Hack and Slash", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Ошибка создания GLFW окна");

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
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
        int texture = loadTexture();

        var view = new Matrix4f().translate(0.0f, 0.0f, -3.0f);
        var projection = new Matrix4f().perspective(toRadians(45f), width / height, 0.1f, 100.0f);

        Vector3f[] cubesPositions = {
                new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(2.0f, 5.0f, -15.0f),
                new Vector3f(-1.5f, -2.2f, -2.5f),
                new Vector3f(-3.8f, -2.0f, -12.3f),
                new Vector3f(2.4f, -0.4f, -3.5f),
                new Vector3f(-1.7f, 3.0f, -7.5f),
                new Vector3f(1.3f, -2.0f, -2.5f),
                new Vector3f(1.5f, 2.0f, -2.5f),
                new Vector3f(1.5f, 0.2f, -1.5f),
                new Vector3f(-1.3f, 1.0f, -1.5f)
        };

        glEnable(GL_DEPTH_TEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glBindTexture(GL_TEXTURE_2D, texture);

            shader.use();

            glUniformMatrix4fv(shader.getUniformLocation("view"), false, view.get(new float[16]));
            glUniformMatrix4fv(shader.getUniformLocation("projection"), false, projection.get(new float[16]));

            glBindVertexArray(vao);
            for (int i = 0; i < 10; i++) {
                var model = new Matrix4f()
                        .translate(cubesPositions[i])
                        .rotate((Math.toRadians(i % 3 == 0 || i == 1 ? (float) (glfwGetTime() * 50) : 20f * i)), 1f, 0.3f, 0.5f);
                glUniformMatrix4fv(shader.getUniformLocation("model"), false, model.get(new float[16]));
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }
            glBindVertexArray(0);

            glfwSwapBuffers(windowHandle);
        }

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }

    private float[] vertices() {
        return new float[]{
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
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

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 5 * Float.BYTES, (3 * Float.BYTES));
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private int loadTexture() {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        var imageData = fileLoadService.loadImageData("textures/angryAsFuck.png");
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imageData.getWidth(), imageData.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData.getImageBytes());
        glGenerateMipmap(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);

        return texture;
    }
}
