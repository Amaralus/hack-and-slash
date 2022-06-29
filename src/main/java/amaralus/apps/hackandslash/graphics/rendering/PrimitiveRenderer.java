package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.graphics.gpu.shader.Shader;
import amaralus.apps.hackandslash.graphics.gpu.shader.ShaderRepository;
import amaralus.apps.hackandslash.graphics.primitives.Line;
import amaralus.apps.hackandslash.graphics.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.primitives.Triangle;
import amaralus.apps.hackandslash.scene.Camera;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import static amaralus.apps.hackandslash.graphics.rendering.RenderComponentType.PRIMITIVE;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.mat4;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec3;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

@Component
public class PrimitiveRenderer implements Renderer {

    private final Shader primitiveShader;

    public PrimitiveRenderer(ShaderRepository shaderRepository) {
        primitiveShader = shaderRepository.get("primitive");
    }

    @Override
    public void render(Camera camera, RenderComponent renderComponent, Vector2f position) {
        if (!(renderComponent instanceof Primitive))
            throw new IllegalArgumentException("expected: Primitive, was: " + renderComponent.getClass().getSimpleName());
        var primitive = (Primitive) renderComponent;

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

    @Override
    public RenderComponentType getRenderComponentType() {
        return PRIMITIVE;
    }

    private void renderLine() {
        glDrawArrays(GL_LINES, 0, 2);
    }

    private void renderTriangle() {
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
