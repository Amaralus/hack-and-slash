package amaralus.apps.hackandslash.resources.factory;

import amaralus.apps.hackandslash.graphics.Shader;
import amaralus.apps.hackandslash.graphics.data.Texture;
import amaralus.apps.hackandslash.graphics.data.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.data.VertexBufferObject;
import amaralus.apps.hackandslash.graphics.data.sprites.SimpleSprite;
import amaralus.apps.hackandslash.graphics.data.sprites.SpriteSheet;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import amaralus.apps.hackandslash.resources.ResourceManager;

import static amaralus.apps.hackandslash.services.ServiceLocator.getService;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

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

    public Shader produceShader(String shaderName) {
        var shader = shaderFactory.produce(shaderName);
        resourceManager.addResource(shaderName, shader);
        return shader;
    }

    public Texture produceTexture(String textureName) {
        var texture = textureFactory.produce(textureName);
        resourceManager.addResource(textureName, texture);
        return texture;
    }

    public SimpleSprite produceSimpleSprite(String spriteName) {
        var texture = produceTexture(spriteName);
        var vao = produceTextureVao(spriteName, produceTextureVbo(spriteName, 1f, 1f));

        var sprite = new SimpleSprite(texture, vao);
        resourceManager.addResource(spriteName, sprite);
        return sprite;
    }

    public SpriteSheet produceSpriteSheet(String spriteName) {
        var texture = produceTexture(spriteName);
        var spriteSheetData = getService(FileLoadService.class)
                .loadFromJson("sprites/data/" + spriteName + ".json", SpriteSheetData.class);

        var frameTexturePosition = SpriteSheet.frameTexturePosition(texture, spriteSheetData);
        var vao = produceTextureVao(spriteName, produceTextureVbo(spriteName, frameTexturePosition.x, frameTexturePosition.y));

        var sprite = new SpriteSheet(texture, vao, spriteSheetData);
        resourceManager.addResource(spriteName, sprite);
        return sprite;
    }

    public VertexBufferObject produceVbo(String vboName, float[] buffer) {
        return produceVbo(vboName + "Vbo", new VertexBufferObject(GL_ARRAY_BUFFER, buffer));
    }

    public VertexBufferObject produceEbo(String eboName, int[] buffer) {
        return produceVbo(eboName + "Ebo", new VertexBufferObject(GL_ELEMENT_ARRAY_BUFFER, buffer));
    }

    private VertexBufferObject produceVbo(String vboName, VertexBufferObject vbo) {
        resourceManager.addResource(vboName, vbo);
        return vbo;
    }

    private VertexBufferObject produceTextureVbo(String vboName, float textureXPosition, float textureYPosition) {
        return produceVbo(vboName, new float[]{0f, textureYPosition, 0f, 0f, textureXPosition, 0f, textureXPosition, textureYPosition});
    }

    public VertexArraysObject produceTextureVao(String vaoName, VertexBufferObject vbo) {
        var vao = new VertexArraysObject(
                vbo,
                resourceManager.getResource("defaultTextureEbo", VertexBufferObject.class)
        );
        resourceManager.addResource(vaoName + "Vao", vao);
        return vao;
    }
}
