package amaralus.apps.hackandslash.config;

import amaralus.apps.hackandslash.common.ApplicationLoader;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("amaralus.apps.hackandslash")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Bean
    public Window window(WindowProperties properties) {
        var window = new Window(properties.getWidth(), properties.getHeight(), properties.getTitle() + " v-0.2.1");
        window.show();
        return window;
    }

    @Bean
    public ApplicationLoader applicationLoader(FileLoadService fileLoadService, ResourceFactory resourceFactory) {
        var loader = new ApplicationLoader(fileLoadService, resourceFactory);
        loader.initLoading();
        return loader;
    }
}
