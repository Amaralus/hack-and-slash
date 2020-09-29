package amaralus.apps.hackandslash.io;

import amaralus.apps.hackandslash.io.entities.KeyCode;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class InputHandler {

    private final EnumSet<KeyCode> pressedKeys;
    private final Map<KeyCode, Runnable> keyActions;

    public InputHandler() {
        pressedKeys = EnumSet.noneOf(KeyCode.class);
        keyActions = new EnumMap<>(KeyCode.class);
    }

    public void handleKeyEvents(KeyEvent event) {
        if (GLFW_PRESS == event.getAction())
            setPressed(event.getKey());
        if (GLFW_RELEASE == event.getAction())
            setReleased(event.getKey());
    }

    public void executeKeyActions() {
        for (var entry : keyActions.entrySet())
            if (isPressed(entry.getKey()))
                entry.getValue().run();
    }

    public void setPressed(KeyCode keyCode) {
        pressedKeys.add(keyCode);
    }

    public void setReleased(KeyCode keyCode) {
        pressedKeys.remove(keyCode);
    }

    public boolean isPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public void releasePressedKeys() {
        pressedKeys.clear();
    }

    public void addAction(KeyCode keyCode, Runnable action) {
        keyActions.put(keyCode, action);
    }

    public void removeAction(KeyCode keyCode) {
        keyActions.remove(keyCode);
    }

    public void clearKeyActions() {
        keyActions.clear();
    }
}
