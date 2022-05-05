package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexArraysObject;
import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexBufferObject;
import amaralus.apps.hackandslash.graphics.gpu.buffer.repository.VaoRepository;
import amaralus.apps.hackandslash.graphics.gpu.buffer.repository.VboRepository;
import amaralus.apps.hackandslash.graphics.gpu.shader.ShaderFactory;
import amaralus.apps.hackandslash.graphics.gpu.shader.ShaderRepository;
import amaralus.apps.hackandslash.graphics.gpu.texture.Texture;
import amaralus.apps.hackandslash.graphics.primitives.Line;
import amaralus.apps.hackandslash.graphics.primitives.Triangle;
import amaralus.apps.hackandslash.graphics.sprites.Sprite;
import amaralus.apps.hackandslash.graphics.sprites.repository.SpriteRepository;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.data.SpriteSheetData;
import amaralus.apps.hackandslash.graphics.gpu.texture.repository.TextureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferType.ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferType.ELEMENT_ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferUsage.DYNAMIC_DRAW;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferUsage.STATIC_DRAW;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.factory.VaoFactory.newVao;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.factory.VboFactory.floatBuffer;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.factory.VboFactory.intBuffer;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.toArray;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceFactory {

    private final FileLoadService fileLoadService;
    private final ShaderFactory shaderFactory;
    private final ShaderRepository shaderRepository;
    private final TextureRepository textureRepository;
    private final VaoRepository vaoRepository;
    private final VboRepository vboRepository;
    private final SpriteRepository spriteRepository;

    public void produceShader(String shaderName) {
        var shader = shaderFactory.produce(shaderName);
        shaderRepository.save(shader);
    }

    public Line produceLine(String name, Color color, Vector2f start, Vector2f end) {
        return new Line(primitiveVao(name, color, start, end), color, start, end);
    }

    public Triangle produceTriangle(String name, Color color, Vector2f topPoint, Vector2f bottomRightPoint, Vector2f bottomLeftPoint) {
        var vao = primitiveVao(name, color, topPoint, bottomRightPoint, bottomLeftPoint);
        return new Triangle(vao, color, topPoint, bottomRightPoint, bottomLeftPoint);
    }

    private VertexArraysObject primitiveVao(String name, Color color, Vector2f... vectors) {
        return newVao()
                .buffer(primitiveBuffer(name, vectors))
                .buffer(colorBuffer(name, color, vectors.length))
                .saveAsVao(name, vaoRepository)
                .build();
    }

    private VertexBufferObject primitiveBuffer(String name, Vector2f... vectors) {
        return floatBuffer(toArray(vectors))
                .type(ARRAY_BUFFER)
                .usage(DYNAMIC_DRAW)
                .saveAsVbo(name, vboRepository)
                .dataFormat(0, 2, 2, 0, Float.TYPE)
                .build();
    }

    private VertexBufferObject colorBuffer(String name, Color color, int countVertex) {
        Vector4f[] colors = new Vector4f[countVertex];
        for (int i = 0; i < colors.length; i++)
            colors[i] = color.rgba();

        return floatBuffer(toArray(colors))
                .type(ARRAY_BUFFER)
                .usage(DYNAMIC_DRAW)
                .saveAsVbo(name + "Color", vboRepository)
                .dataFormat(1, 4, 4, 0, Float.TYPE)
                .build();
    }

    public void produceSprite(String spriteName) {
        var texture = textureRepository.get(spriteName);
        var spriteSheetData = fileLoadService.loadFromJson("sprites/data/" + spriteName + ".json", SpriteSheetData.class);

        var vao = newVao()
                .buffer(vboRepository.get("defaultTextureEbo"))
                .buffer(floatBuffer(textureData(texture, spriteSheetData))
                        .type(ARRAY_BUFFER)
                        .usage(STATIC_DRAW)
                        .saveAsVbo(spriteName, vboRepository)
                        .dataFormat(0, 2, 2, 0, Float.TYPE))
                .saveAsVao(spriteName, vaoRepository)
                .build();

        var sprite = new Sprite(spriteName, texture, vao, spriteSheetData);
        spriteRepository.save(sprite);
    }

    private float[] textureData(Texture texture, SpriteSheetData spriteSheetData) {
        var texturePosition = Sprite.frameTexturePosition(texture, spriteSheetData);
        return new float[]{0f, texturePosition.y, 0f, 0f, texturePosition.x, 0f, texturePosition.x, texturePosition.y};
    }

    public void produceDefaultTextureEbo() {
        intBuffer(0, 1, 3, 1, 2, 3)
                .type(ELEMENT_ARRAY_BUFFER)
                .usage(STATIC_DRAW)
                .saveAsEbo("defaultTexture", vboRepository)
                .build();
    }
}
