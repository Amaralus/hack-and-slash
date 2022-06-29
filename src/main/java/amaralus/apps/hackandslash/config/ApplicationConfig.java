package amaralus.apps.hackandslash.config;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.rendering.RenderComponentType;
import amaralus.apps.hackandslash.graphics.rendering.Renderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
@ComponentScan("amaralus.apps.hackandslash")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Bean
    public Window window(WindowProperties properties) {
        var window = new Window(properties.getWidth(), properties.getHeight(), properties.getTitle() + " v-0.2.9");
        window.show();
        return window;
    }

    @Bean
    public Map<RenderComponentType, Renderer> renderers(Set<Renderer> rendererSet) {
        return rendererSet.stream()
                .collect(toMap(
                        Renderer::getRenderComponentType,
                        Function.identity()
                ));
    }
}
