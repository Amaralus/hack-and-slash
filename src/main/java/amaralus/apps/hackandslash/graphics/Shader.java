package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.Destroyable;
import amaralus.apps.hackandslash.io.FileLoadService;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Shader implements Destroyable {

    private static final Logger log = LoggerFactory.getLogger(Shader.class);

    private final int program;

    public Shader(String shaderName) {
        program = glCreateProgram();

        int vertexShaderId = loadShader(GL_VERTEX_SHADER, shaderName + "Vertex");
        int fragmentShaderId = loadShader(GL_FRAGMENT_SHADER, shaderName + "Fragment");

        glAttachShader(program, vertexShaderId);
        glAttachShader(program, fragmentShaderId);
        glLinkProgram(program);

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);

        checkResult(glGetProgramInfoLog(program));
        log.debug("Шейдер {} успешно слинкован", shaderName);
    }

    @Override
    public void destroy() {
        glDeleteProgram(program);
    }

    public void use() {
        glUseProgram(program);
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(program, name);
    }

    private int loadShader(int type, String name) {
        var fileName = "shaders/" + name + ".glsl";
        int shader = glCreateShader(type);

        glShaderSource(shader, getService(FileLoadService.class).loadFileAsString(fileName));
        glCompileShader(shader);

        checkResult(glGetShaderInfoLog(shader));
        log.debug("Шейдер {} успешно скомпилирован", name);

        return shader;
    }

    private void checkResult(String result) {
        if (!result.isEmpty())
            throw new ShaderLoadException(result);
    }

    public int getProgram() {
        return program;
    }

    public void setVec2(String parameter, Vector2f vector) {
        glUniform2f(getUniformLocation(parameter), vector.x, vector.y);
    }

    public void setMat4(String parameter, Matrix4f matrix) {
        glUniformMatrix4fv(getUniformLocation(parameter), false, matrix.get(BufferUtils.createFloatBuffer(16)));
    }

    public void setInt(String parameter, int i) {
        glUniform1i(getUniformLocation(parameter), i);
    }

    public static final class ShaderLoadException extends RuntimeException {
        public ShaderLoadException(String message) {
            super(message);
        }
    }
}
