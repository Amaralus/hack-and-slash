package amaralus.apps.hackandslash.common;

import amaralus.apps.hackandslash.gameplay.GameplayManager;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.resources.ResourceManager;
import amaralus.apps.hackandslash.resources.factory.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static amaralus.apps.hackandslash.common.ServiceLocator.getService;
import static amaralus.apps.hackandslash.common.ServiceLocator.registerService;

public class ApplicationLoader {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLoader.class);

    public void initLoading() {
        loadTechServices();
        loadShades();
        loadTextures();
        loadSprites();
        loadGameplayServices();
    }

    private void loadTechServices() {
        log.info("Загрузка технических сервисов...");
        registerService(new FileLoadService());
        registerService(new ResourceManager());
        registerService(new ResourceFactory(getService(ResourceManager.class)));
    }

    private void loadGameplayServices() {
        log.info("Загрузка сервисов геймплея...");
        registerService(new GameplayManager(getService(Window.class)));
    }

    private void loadShades() {
        log.info("Загрузка шейдеров...");
        var resourceFactory = getService(ResourceFactory.class);
        getService(FileLoadService.class)
                .loadFileNamesFromDirectory("shaders").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .collect(Collectors.toSet())
                .forEach(resourceFactory::produceShader);
    }

    private void loadTextures() {
        log.info("Загрузка текстур...");
        var resourceFactory = getService(ResourceFactory.class);
        getService(FileLoadService.class)
                .loadFileNamesFromDirectory("sprites").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .forEach(resourceFactory::produceTexture);
    }

    private void loadSprites() {
        log.info("Загрузка спрайтов...");
        var resourceFactory = getService(ResourceFactory.class);
        getService(FileLoadService.class)
                .loadFileNamesFromDirectory("sprites/data").stream()
                .map(name -> name.substring(0, name.indexOf('.')))
                .forEach(resourceFactory::produceSprite);
    }

}
