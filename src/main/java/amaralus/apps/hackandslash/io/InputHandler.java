package amaralus.apps.hackandslash.io;

import amaralus.apps.hackandslash.io.entities.KeyCode;

import java.util.EnumSet;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class InputHandler {

    private final EnumSet<KeyCode> pressedKeys;

    public InputHandler() {
        pressedKeys = EnumSet.noneOf(KeyCode.class);
    }

    public void handleKeyEvents(KeyEvent event) {
        if (GLFW_PRESS == event.getAction())
            setPressed(event.getKey());
        if (GLFW_RELEASE == event.getAction())
            setReleased(event.getKey());
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
}
