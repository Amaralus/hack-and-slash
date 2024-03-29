package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.graphics.gpu.shader.Shader;
import amaralus.apps.hackandslash.graphics.gpu.shader.ShaderRepository;
import amaralus.apps.hackandslash.graphics.primitives.Line;
import amaralus.apps.hackandslash.graphics.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.primitives.Triangle;
import amaralus.apps.hackandslash.graphics.scene.Camera;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.mat4;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec3;
import static org.lwjgl.opengl.GL11.*;

@Component
public class PrimitiveRenderer {

    private final Shader primitiveShader;

    public PrimitiveRenderer(ShaderRepository shaderRepository) {
        primitiveShader = shaderRepository.get("primitive");
    }

    public void render(Camera camera, Primitive primitive, Vector2f position) {

        primitiveShader.use();
        primitiveShader.setMat4("model", mat4().translate(vec3(camera.getEntityCamPos(position), 1f)));
        primitiveShader.setMat4("projection", camera.getProjection());

        primitive.getVao().bind();

        if (primitive instanceof Line)
            renderLine();
        else if (primitive instanceof Triangle)
            renderTriangle();

        primitive.getVao().unbind();
    }

    private void renderLine() {
        glDrawArrays(GL_LINES, 0, 2);
    }

    private void renderTriangle() {
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
