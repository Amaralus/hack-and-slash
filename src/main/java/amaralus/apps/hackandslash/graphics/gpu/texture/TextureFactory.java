package amaralus.apps.hackandslash.graphics.gpu.texture;

import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.data.ImageData;
import amaralus.apps.hackandslash.resources.ResourceManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static amaralus.apps.hackandslash.graphics.gpu.texture.PixelFormat.RED;
import static amaralus.apps.hackandslash.graphics.gpu.texture.PixelFormat.RGBA;
import static amaralus.apps.hackandslash.graphics.gpu.texture.TextureFilter.*;
import static amaralus.apps.hackandslash.graphics.gpu.texture.TextureParameterName.*;
import static amaralus.apps.hackandslash.graphics.gpu.texture.TextureWrapMode.REPEAT;

@Component
@RequiredArgsConstructor
@Slf4j
public class TextureFactory {

    private final ResourceManager resourceManager;
    private final FileLoadService fileLoadService;

    public void produceSpriteTexture(String textureName) {
        logTextureLoad(textureName);

        var imageData = fileLoadService.loadImageData("sprites/" + textureName + ".png");

        var texture = new TextureBuilder(textureName)
                .imageData(imageData)
                .pixelFormat(RGBA)
                .generateMipmap()
                .param(WRAP_S, REPEAT)
                .param(WRAP_T, REPEAT)
                .param(MIN_FILTER, NEAREST)
                .param(MAG_FILTER, NEAREST)
                .produce();

        resourceManager.addResource(texture);
    }

    public Texture produceFontTexture(String textureName, ImageData imageData) {
        logTextureLoad(textureName);

        var texture = new TextureBuilder(textureName)
                .imageData(imageData)
                .pixelFormat(RED)
                .generateMipmap()
                .param(WRAP_S, CLAMP_TO_EDGE)
                .param(WRAP_T, CLAMP_TO_EDGE)
                .param(MIN_FILTER, LINEAR)
                .param(MAG_FILTER, LINEAR)
                .produce();

        resourceManager.addResource(texture);
        return texture;
    }

    private void logTextureLoad(String textureName) {
        log.debug("Загрузка текстуры {}", textureName);
    }
}
