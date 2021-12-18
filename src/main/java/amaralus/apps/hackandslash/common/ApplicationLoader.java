package amaralus.apps.hackandslash.common;

import amaralus.apps.hackandslash.graphics.font.FontFactory;
import amaralus.apps.hackandslash.graphics.gpu.texture.TextureFactory;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@DependsOn("window")
public class ApplicationLoader {

    private final FileLoadService fileLoadService;
    private final ResourceFactory resourceFactory;
    private final TextureFactory textureFactory;
    private final FontFactory fontFactory;

    public ApplicationLoader(FileLoadService fileLoadService, ResourceFactory resourceFactory, TextureFactory textureFactory, FontFactory fontFactory) {
        this.fileLoadService = fileLoadService;
        this.resourceFactory = resourceFactory;
        this.textureFactory = textureFactory;
        this.fontFactory = fontFactory;

        loadShades();
    }

    public void initLoading() {
        resourceFactory.produceDefaultTextureEbo();
        loadFonts();
        loadTextures();
        loadSprites();
    }

    private void loadShades() {
        log.info("Загрузка шейдеров...");
        fileLoadService.loadFileNamesFromDirectory("shaders").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .collect(Collectors.toSet())
                .forEach(resourceFactory::produceShader);
    }

    private void loadFonts() {
        log.info("Загрузка шрифтов...");
        fileLoadService.loadFileNamesFromDirectory("fonts").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .collect(Collectors.toSet())
                .forEach(fontFactory::produceFont);
    }

    private void loadTextures() {
        log.info("Загрузка текстур...");
        fileLoadService.loadFileNamesFromDirectory("sprites").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .forEach(textureFactory::produceSpriteTexture);
    }

    private void loadSprites() {
        log.info("Загрузка спрайтов...");
        fileLoadService.loadFileNamesFromDirectory("sprites/data").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .forEach(resourceFactory::produceSprite);
    }

}
