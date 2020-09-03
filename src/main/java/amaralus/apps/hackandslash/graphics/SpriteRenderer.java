package amaralus.apps.hackandslash.graphics;

import amaralus.apps.hackandslash.graphics.camera.OrthoCamera;
import amaralus.apps.hackandslash.graphics.data.Sprite;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static amaralus.apps.hackandslash.VectMatrUtil.*;
import static org.joml.Math.toRadians;
import static org.lwjgl.opengl.GL15.*;

public class SpriteRenderer {

    private final Shader textureShader;

    public SpriteRenderer() {
        textureShader = new Shader("vertex", "fragment");
    }

    public void draw(OrthoCamera camera, Sprite sprite, Vector2f entityPos, float rotateAngle) {
        var textureSize = camera.getSpriteScaleOfCam(sprite);
        var cameraEntityPos = camera.getEntityCamPos(entityPos, textureSize);

        textureShader.use();
        textureShader.setVec2("offset", sprite.getTextureOffset());
        textureShader.setMat4("model", calcModel(textureSize, cameraEntityPos, rotateAngle));
        textureShader.setMat4("projection", camera.getProjection());

        sprite.getTexture().bind();
        sprite.getVao().bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        sprite.getVao().unbind();
    }

    private Matrix4f calcModel(Vector2f textureSize, Vector2f cameraEntityPos, float rotateAngle) {
        var model = mat4().translate(vec3(cameraEntityPos, 1f));
        rotateModel(model, textureSize, rotateAngle);
        return model.scale(vec3(textureSize, 1f));
    }

    private void rotateModel(Matrix4f model, Vector2f textureSize, float rotateAngle) {
        if (rotateAngle != 0)
            model.translate(vec3(0.5f * textureSize.x, 0.5f * textureSize.y, 0f))
                    .rotate(toRadians(rotateAngle), vec3(0f, 0f, 1f))
                    .translate(vec3(-0.5f * textureSize.x, -0.5f * textureSize.y, 0f));
    }
}
