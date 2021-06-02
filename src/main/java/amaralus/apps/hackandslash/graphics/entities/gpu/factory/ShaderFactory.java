package amaralus.apps.hackandslash.graphics.entities.gpu.factory;

import amaralus.apps.hackandslash.graphics.entities.gpu.Shader;
import amaralus.apps.hackandslash.io.FileLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.lwjgl.opengl.GL20.*;

@Component
public class ShaderFactory {

    private static final Logger log = LoggerFactory.getLogger(ShaderFactory.class);

    private final FileLoadService fileLoadService;

    public ShaderFactory(FileLoadService fileLoadService) {
        this.fileLoadService = fileLoadService;
    }

    public Shader produce(String name) {
        log.debug("Загрузка шейдера {}", name);

        int program = glCreateProgram();

        int vertexShaderId = loadShader(GL_VERTEX_SHADER, name + ".vsh");
        int fragmentShaderId = loadShader(GL_FRAGMENT_SHADER, name + ".frsh");

        linkShader(program, vertexShaderId, fragmentShaderId);

        return new Shader(name, program);
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
