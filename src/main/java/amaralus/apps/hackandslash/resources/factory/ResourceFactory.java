package amaralus.apps.hackandslash.resources.factory;

import amaralus.apps.hackandslash.graphics.entities.data.Texture;
import amaralus.apps.hackandslash.graphics.entities.data.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.entities.data.VertexBufferObject;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import amaralus.apps.hackandslash.resources.ResourceManager;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
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
