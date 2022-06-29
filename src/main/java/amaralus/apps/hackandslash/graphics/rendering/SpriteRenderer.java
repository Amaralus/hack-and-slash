package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.graphics.gpu.shader.Shader;
import amaralus.apps.hackandslash.graphics.gpu.shader.ShaderRepository;
import amaralus.apps.hackandslash.graphics.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.scene.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import static amaralus.apps.hackandslash.graphics.rendering.RenderComponentType.SPRITE;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.mat4;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec3;
import static org.joml.Math.toRadians;
import static org.lwjgl.opengl.GL15.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.glDrawElements;

@Component
public class SpriteRenderer implements Renderer {

    private final Shader textureShader;

    public SpriteRenderer(ShaderRepository shaderRepository) {
        this.textureShader = shaderRepository.get("texture");
    }

    public void render(Camera camera, RenderComponent renderComponent, Vector2f entityPos) {
        if (!(renderComponent instanceof SpriteRenderComponent))
            throw new IllegalArgumentException("expected: SpriteRenderComponent, was: " + renderComponent.getClass().getSimpleName());
        var spriteRenderComponent = (SpriteRenderComponent) renderComponent;

        var sprite = spriteRenderComponent.getSprite();
        var textureSize = sprite.getSize();
        var cameraEntityPos = camera.getEntityCamPos(entityPos, textureSize, sprite.getOffsetToSpriteCenter());

        textureShader.use();
        textureShader.setVec2("offset", spriteRenderComponent.getTextureOffset());
        textureShader.setMat4("model", calcModel(
                textureSize,
                cameraEntityPos,
                spriteRenderComponent.getCurrentFrame().getSize(),
                spriteRenderComponent.getSpriteRotateAngle()));
        textureShader.setMat4("projection", camera.getProjection());

        sprite.getTexture().bind();
        sprite.getVao().bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        sprite.getVao().unbind();
    }

    @Override
    public RenderComponentType getRenderComponentType() {
        return SPRITE;
    }

    private Matrix4f calcModel(Vector2f textureSize, Vector2f cameraEntityPos, Vector2f frameSize, float rotateAngle) {
        var model = mat4().translate(vec3(cameraEntityPos, 1f));
        rotateModel(model, frameSize, rotateAngle);
        return model.scale(vec3(textureSize, 1f));
    }

    private void rotateModel(Matrix4f model, Vector2f frameSize, float rotateAngle) {
        if (rotateAngle != 0)
            model.translate(vec3(0.5f * frameSize.x, 0.5f * frameSize.y, 0f))
                    .rotate(toRadians(rotateAngle), vec3(0f, 0f, 1f))
                    .translate(vec3(-0.5f * frameSize.x, -0.5f * frameSize.y, 0f));
    }
}
