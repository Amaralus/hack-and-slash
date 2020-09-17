package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.resources.ResourceManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.*;
import static org.joml.Math.toRadians;
import static org.lwjgl.opengl.GL15.*;

public class SpriteRenderer {

    private final Camera camera;
    private final Shader textureShader;

    public SpriteRenderer(Camera camera) {
        this.camera = camera;
        textureShader = getService(ResourceManager.class).getResource("texture", Shader.class);
    }

    public void render(RenderEntity renderEntity, Vector2f entityPos) {
        var textureSize = camera.getSpriteScaleOfCam(renderEntity.getSprite());
        var cameraEntityPos = camera.getEntityCamPos(entityPos, textureSize, renderEntity.getSprite().getOffsetToSpriteCenter());

        textureShader.use();
        textureShader.setVec2("offset", renderEntity.getTextureOffset());
        textureShader.setMat4("model", calcModel(
                textureSize,
                cameraEntityPos,
                camera.getFrameScaleOfCam(renderEntity.getCurrentFrame()),
                renderEntity.getSpriteRotateAngle()));
        textureShader.setMat4("projection", camera.getProjection());

        renderEntity.getSprite().getTexture().bind();
        renderEntity.getSprite().getVao().bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        renderEntity.getSprite().getVao().unbind();
    }

    private Matrix4f calcModel(Vector2f textureSize, Vector2f cameraEntityPos, Vector2f frameSize, float rotateAngle) {
        var model = mat4().translate(vec3(cameraEntityPos, 1f));
        rotateModel(model, frameSize, rotateAngle);
        return model.scale(vec3(textureSize, 1f));
    }

    private void rotateModel(Matrix4f model, Vector2f textureSize, float rotateAngle) {
        if (rotateAngle != 0)
            model.translate(vec3(0.5f * textureSize.x, 0.5f * textureSize.y, 0f))
                    .rotate(toRadians(rotateAngle), vec3(0f, 0f, 1f))
                    .translate(vec3(-0.5f * textureSize.x, -0.5f * textureSize.y, 0f));
    }
}
