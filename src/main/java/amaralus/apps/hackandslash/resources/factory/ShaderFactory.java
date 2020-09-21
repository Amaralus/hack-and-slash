package amaralus.apps.hackandslash.resources.factory;

import amaralus.apps.hackandslash.graphics.entities.Shader;
import amaralus.apps.hackandslash.io.FileLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static org.lwjgl.opengl.GL20.*;

public class ShaderFactory {

    private static final Logger log = LoggerFactory.getLogger(ShaderFactory.class);

    private final FileLoadService fileLoadService;

    public ShaderFactory() {
        fileLoadService = getService(FileLoadService.class);
    }

    public Shader produce(String name) {
        log.info("Загрузка шейдера {}", name);

        int program = glCreateProgram();

        int vertexShaderId = loadShader(GL_VERTEX_SHADER, name + "Vertex");
        int fragmentShaderId = loadShader(GL_FRAGMENT_SHADER, name + "Fragment");

        linkShader(program, vertexShaderId, fragmentShaderId);

        return new Shader(program);
    }

    private int loadShader(int type, String name) {
        var fileName = "shaders/" + name + ".glsl";
        int shader = glCreateShader(type);

        glShaderSource(shader, fileLoadService.loadFileAsString(fileName));
        glCompileShader(shader);

        checkResult(glGetShaderInfoLog(shader));
        log.debug("Шейдер {} успешно скомпилирован", name);

        return shader;
    }

    private void linkShader(int program, int vertexShaderId, int fragmentShaderId) {
        glAttachShader(program, vertexShaderId);
        glAttachShader(program, fragmentShaderId);
        glLinkProgram(program);

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);

        checkResult(glGetProgramInfoLog(program));
    }

    private void checkResult(String result) {
        if (!result.isEmpty())
            throw new ShaderLoadException(result);
    }

    public static final class ShaderLoadException extends RuntimeException {
        public ShaderLoadException(String message) {
            super(message);
        }
    }
}
