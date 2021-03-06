package amaralus.apps.hackandslash.graphics.entities.gpu;

import amaralus.apps.hackandslash.resources.Resource;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Shader extends Resource {

    private final int program;

    public Shader(String resourceName, int shaderProgramId) {
        super(resourceName);
        program = shaderProgramId;
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
}
