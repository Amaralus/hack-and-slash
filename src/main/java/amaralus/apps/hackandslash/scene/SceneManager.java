package amaralus.apps.hackandslash.scene;

import amaralus.apps.hackandslash.graphics.Window;
import lombok.Getter;
import org.joml.Vector2f;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.graphics.Color.WHITE;

@Service
public class SceneManager {

    private final Window window;
    @Getter
    private final Scene activeScene;

    public SceneManager(Window window) {
        activeScene = new Scene(window.getWidth(), window.getHeight());
        activeScene.setBackgroundColor(WHITE);
        this.window = window;
    }

    public Vector2f getGlobalCursorPosition() {
        return activeScene.getCamera().getWordPosOfScreenPos(window.getCursorPosition());
    }
}
