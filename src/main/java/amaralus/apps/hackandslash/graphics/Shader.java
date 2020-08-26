package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.io.FileLoadService;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Shader {

    private static final Logger log = LoggerFactory.getLogger(Shader.class);

    private final int program;

    public Shader(String vertexShaderName, String fragmentShaderName) {
        program = glCreateProgram();

        var fileLoadService = new FileLoadService();
        int vertexShader = loadShader(fileLoadService, GL_VERTEX_SHADER, vertexShaderName);
        int fragmentShader = loadShader(fileLoadService, GL_FRAGMENT_SHADER, fragmentShaderName);

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        var linkLog = resultLog(glGetProgramInfoLog(program));
        log.debug("Результат линковки программы шейдеров: {}", linkLog);
    }

    public void use() {
        glUseProgram(program);
    }

    public void setMatrix4(String parameter, Matrix4f matrix) {
        glUniformMatrix4fv(getUniformLocation(parameter), false, matrix.get(BufferUtils.createFloatBuffer(16)));
    }

    public void setInteger(String parameter, int i) {
        glUniform1i(getUniformLocation(parameter), i);
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(program, name);
    }

    private int loadShader(FileLoadService fileLoadService, int type, String name) {
        var fileName = "shaders/" + name + ".glsl";
        int shader = glCreateShader(type);

        glShaderSource(shader, fileLoadService.loadFileAsString(fileName));
        glCompileShader(shader);

        var compileLog = resultLog(glGetShaderInfoLog(shader));
        log.debug("Результат компиляции шейдера {}: {}", name, compileLog);

        return shader;
    }

    private String resultLog(String logString) {
        return logString.isEmpty() ? "successful" : logString;
    }

    public int getProgram() {
        return program;
    }
}
