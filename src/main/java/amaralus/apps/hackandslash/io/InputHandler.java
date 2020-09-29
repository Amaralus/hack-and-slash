package amaralus.apps.hackandslash.io;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.io.entities.KeyCode;
import amaralus.apps.hackandslash.io.entities.KeyEvent;
import amaralus.apps.hackandslash.io.entities.ScrollEvent;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class InputHandler {

    private final EnumSet<KeyCode> pressedKeys;
    private final Map<KeyCode, Runnable> keyActions;

    private BiConsumer<Float, Float> scrollAction;
    private float scrollXOffset;
    private float scrollYOffset;

    public InputHandler() {
        pressedKeys = EnumSet.noneOf(KeyCode.class);
        keyActions = new EnumMap<>(KeyCode.class);
    }

    public void setUpInputHandling(Window window) {
        window.setKeyCallback(this::handleKeyEvents);
        window.setScrollCallback(this::handleScrollEvents);
    }

    public void handleKeyEvents(KeyEvent event) {
        if (GLFW_PRESS == event.getAction())
            setPressed(event.getKey());
        if (GLFW_RELEASE == event.getAction())
            setReleased(event.getKey());
    }

    public void handleScrollEvents(ScrollEvent event) {
        scrollXOffset = (float) event.getxOffset();
        scrollYOffset = (float) event.getyOffset();
    }

    public void executeActions() {
        executeKeyActions();
        executeScrollAction();
    }

    private void executeKeyActions() {
        for (var entry : keyActions.entrySet())
            if (isPressed(entry.getKey()))
                entry.getValue().run();
    }

    private void executeScrollAction() {
        if (scrollAction != null && scrollXOffset != 0f && scrollYOffset != 0f) {
            scrollAction.accept(scrollXOffset, scrollYOffset);
            scrollXOffset = 0f;
            scrollYOffset = 0f;
        }
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

    public void setScrollAction(BiConsumer<Float, Float> scrollAction) {
        this.scrollAction = scrollAction;
    }
}
