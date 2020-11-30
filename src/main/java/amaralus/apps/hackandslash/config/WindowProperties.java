package amaralus.apps.hackandslash.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WindowProperties {
    @Value("${window.title}")
    private String title;
    @Value("${window.width}")
    private int width;
    @Value("${window.height}")
    private int height;

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
