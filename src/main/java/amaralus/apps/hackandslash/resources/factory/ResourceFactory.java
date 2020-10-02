package amaralus.apps.hackandslash.resources.factory;

import amaralus.apps.hackandslash.graphics.entities.data.*;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import amaralus.apps.hackandslash.resources.ResourceManager;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static amaralus.apps.hackandslash.graphics.entities.data.BufferType.ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.entities.data.BufferType.ELEMENT_ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.entities.data.BufferUsage.STATIC_DRAW;

public class ResourceFactory {

    private final ResourceManager resourceManager;
    private final ShaderFactory shaderFactory;
    private final TextureFactory textureFactory;

    public ResourceFactory(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        shaderFactory = new ShaderFactory();
        textureFactory = new TextureFactory();

        produceEbo("defaultTexture", new int[]{0, 1, 3, 1, 2, 3});
    }

    public void produceShader(String shaderName) {
        var shader = shaderFactory.produce(shaderName);
        resourceManager.addResource(shaderName, shader);
    }

    public void produceTexture(String textureName) {
        var texture = textureFactory.produce(textureName);
        resourceManager.addResource(textureName, texture);
    }

    public Sprite produceSprite(String spriteName) {
        var texture = resourceManager.getResource(spriteName, Texture.class);
        var spriteSheetData = getService(FileLoadService.class)
                .loadFromJson("sprites/data/" + spriteName + ".json", SpriteSheetData.class);

        var frameTexturePosition = Sprite.frameTexturePosition(texture, spriteSheetData);
        var vao = produceTextureVao(spriteName, produceTextureVbo(spriteName, frameTexturePosition.x, frameTexturePosition.y));

        var sprite = new Sprite(texture, vao, spriteSheetData);
        resourceManager.addResource(spriteName, sprite);
        return sprite;
    }

    public VertexBufferObject<FloatBuffer> produceVbo(String vboName, float[] buffer) {
        return produceVbo(vboName + "Vbo", new FloatVertexBufferObject(ARRAY_BUFFER, STATIC_DRAW, buffer));
    }

    public VertexBufferObject<IntBuffer> produceEbo(String eboName, int[] buffer) {
        return produceVbo(eboName + "Ebo", new IntVertexBufferObject(ELEMENT_ARRAY_BUFFER, STATIC_DRAW, buffer));
    }

    private <B extends Buffer> VertexBufferObject<B> produceVbo(String vboName, VertexBufferObject<B> vbo) {
        resourceManager.addResource(vboName, vbo);
        return vbo;
    }

    private VertexBufferObject<FloatBuffer> produceTextureVbo(String vboName, float textureXPosition, float textureYPosition) {
        return produceVbo(vboName, new float[]{0f, textureYPosition, 0f, 0f, textureXPosition, 0f, textureXPosition, textureYPosition});
    }

    public VertexArraysObject produceTextureVao(String vaoName, VertexBufferObject<FloatBuffer> vbo) {
        var vao = new VertexArraysObject(
                vbo,
                resourceManager.getResource("defaultTextureEbo", IntVertexBufferObject.class)
        );
        resourceManager.addResource(vaoName + "Vao", vao);
        return vao;
    }
}
