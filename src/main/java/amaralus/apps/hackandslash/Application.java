package amaralus.apps.hackandslash;

import amaralus.apps.hackandslash.services.io.FileService;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private long windowHandle;
    private FileService fileService = new FileService();

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

        windowHandle = glfwCreateWindow(600, 400, "Hack and Slash", NULL, NULL);
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

        int vertexShader = loadShader(GL_VERTEX_SHADER, "vertex");
        int fragmentShader = loadShader(GL_FRAGMENT_SHADER, "fragment");

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        log.debug("shader program log: {}", glGetProgramInfoLog(shaderProgram));

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        float[] vertices = {
                0.5f, 0.5f, 0.0f,  // Верхний правый угол
                0.5f, -0.5f, 0.0f,  // Нижний правый угол
                -0.5f, -0.5f, 0.0f,  // Нижний левый угол
                -0.5f, 0.5f, 0.0f   // Верхний левый угол
        };

        int[] indices = {
                0, 1, 3,   // Первый треугольник
                1, 2, 3    // Второй треугольник
        };

        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glUseProgram(shaderProgram);

            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);

            glfwSwapBuffers(windowHandle);
        }

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }

    private int loadShader(int type, String name) {
        String fileName = "shaders/" + name + ".glsl";
        int shader = glCreateShader(type);

        glShaderSource(shader, fileService.loadFileAsStringFromResources(fileName));
        glCompileShader(shader);

        String compileLog = glGetShaderInfoLog(shader);
        log.debug("Результат компиляции шейдера {}: {}", name, compileLog.isEmpty() ? "compilation successful" : compileLog);

        return shader;
    }
}
