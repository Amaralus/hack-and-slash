package amaralus.apps.hackandslash.common;

import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class ApplicationLoader {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLoader.class);

    private final FileLoadService fileLoadService;
    private final ResourceFactory resourceFactory;

    public ApplicationLoader(FileLoadService fileLoadService, ResourceFactory resourceFactory) {
        this.fileLoadService = fileLoadService;
        this.resourceFactory = resourceFactory;
    }

    public void initLoading() {
        loadShades();
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

    private void loadTextures() {
        log.info("Загрузка текстур...");
        fileLoadService.loadFileNamesFromDirectory("sprites").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .forEach(resourceFactory::produceTexture);
    }

    private void loadSprites() {
        log.info("Загрузка спрайтов...");
        fileLoadService.loadFileNamesFromDirectory("sprites/data").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .forEach(resourceFactory::produceSprite);
    }

}
