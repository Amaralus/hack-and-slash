package amaralus.apps.hackandslash.resources.factory;

import amaralus.apps.hackandslash.graphics.entities.data.*;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.entities.SpriteSheetData;
import amaralus.apps.hackandslash.resources.ResourceManager;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static amaralus.apps.hackandslash.graphics.entities.data.BufferType.ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.entities.data.BufferType.ELEMENT_ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.entities.data.BufferUsage.STATIC_DRAW;
import static amaralus.apps.hackandslash.resources.factory.VaoFactory.newVao;
import static amaralus.apps.hackandslash.resources.factory.VboFactory.floatBuffer;
import static amaralus.apps.hackandslash.resources.factory.VboFactory.intBuffer;

public class ResourceFactory {

    private final ResourceManager resourceManager;
    private final ShaderFactory shaderFactory;
    private final TextureFactory textureFactory;

    public ResourceFactory(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        shaderFactory = new ShaderFactory();
        textureFactory = new TextureFactory();

        produceDefaultTextureEbo();
    }

    public void produceShader(String shaderName) {
        var shader = shaderFactory.produce(shaderName);
        resourceManager.addResource(shaderName, shader);
    }

    public void produceTexture(String textureName) {
        var texture = textureFactory.produce(textureName);
        resourceManager.addResource(textureName, texture);
    }

    public void produceSprite(String spriteName) {
        var texture = resourceManager.getResource(spriteName, Texture.class);
        var spriteSheetData = getService(FileLoadService.class)
                .loadFromJson("sprites/data/" + spriteName + ".json", SpriteSheetData.class);

        var vao = newVao()
                .buffer(resourceManager.getResource("defaultTextureEbo", IntVertexBufferObject.class))
                .buffer(floatBuffer(textureData(texture, spriteSheetData))
                        .type(ARRAY_BUFFER)
                        .usage(STATIC_DRAW)
                        .saveAs(spriteName + "Vbo", resourceManager)
                        .build())
                .saveAs(spriteName + "Vao", resourceManager)
                .build();

        var sprite = new Sprite(texture, vao, spriteSheetData);
        resourceManager.addResource(spriteName, sprite);
    }

    private float[] textureData(Texture texture, SpriteSheetData spriteSheetData) {
        var texturePosition = Sprite.frameTexturePosition(texture, spriteSheetData);
        return new float[]{0f, texturePosition.y, 0f, 0f, texturePosition.x, 0f, texturePosition.x, texturePosition.y};
    }

    private void produceDefaultTextureEbo() {
        intBuffer(new int[]{0, 1, 3, 1, 2, 3})
                .type(ELEMENT_ARRAY_BUFFER)
                .usage(STATIC_DRAW)
                .saveAs("defaultTextureEbo", resourceManager)
                .build();
    }
}
