package amaralus.apps.hackandslash.io.events;

import amaralus.apps.hackandslash.graphics.Window;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class InputHandler {

    private final EnumSet<KeyCode> pressedKeys;
    private final Map<KeyCode, Runnable> keyActions;

    private final EnumSet<MouseButton> pressedButtons;
    private final Map<MouseButton, Runnable> buttonActions;

    private BiConsumer<Float, Float> scrollAction;
    private float scrollingSensitivity = 0.1f;
    private float scrollXOffset;
    private float scrollYOffset;

    public InputHandler() {
        pressedKeys = EnumSet.noneOf(KeyCode.class);
        pressedButtons = EnumSet.noneOf(MouseButton.class);

        keyActions = new EnumMap<>(KeyCode.class);
        buttonActions = new EnumMap<>(MouseButton.class);
    }

    public void setUpInputHandling(Window window) {
        window.setKeyCallback(this::handleKeyboardKeyEvents);
        window.setMouseButtonCallback(this::handleMouseButtonsEvents);
        window.setScrollCallback(this::handleScrollEvents);
    }

    public void handleKeyboardKeyEvents(KeyboardKeyEvent event) {
        if (GLFW_PRESS == event.getAction())
            setPressed(event.getKey());
        if (GLFW_RELEASE == event.getAction())
            setReleased(event.getKey());
    }

    public void handleMouseButtonsEvents(MouseButtonEvent event) {
        if (GLFW_PRESS == event.getAction())
            setPressed(event.getButton());
        if (GLFW_RELEASE == event.getAction())
            setReleased(event.getButton());
    }

    public void handleScrollEvents(ScrollEvent event) {
        scrollXOffset = (float) event.getxOffset() * scrollingSensitivity;
        scrollYOffset = (float) event.getyOffset() * scrollingSensitivity;
    }

    public void executeActions() {
        executeKeyActions();
        executeButtonActions();
        executeScrollAction();
    }

    private void executeKeyActions() {
        for (var entry : keyActions.entrySet())
            if (isPressed(entry.getKey()))
                entry.getValue().run();
    }

    private void executeButtonActions() {
        for (var entry : buttonActions.entrySet())
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

    //

    public void setPressed(MouseButton mouseButton) {
        pressedButtons.add(mouseButton);
    }

    public void setReleased(MouseButton mouseButton) {
        pressedButtons.remove(mouseButton);
    }

    public boolean isPressed(MouseButton mouseButton) {
        return pressedButtons.contains(mouseButton);
    }

    public void releasePressedMouseButtons() {
        pressedButtons.clear();
    }

    public void addAction(MouseButton mouseButton, Runnable action) {
        buttonActions.put(mouseButton, action);
    }

    public void removeAction(MouseButton mouseButton) {
        buttonActions.remove(mouseButton);
    }

    public void clearButtonActions() {
        buttonActions.clear();
    }

    public void setScrollingSensitivity(float scrollingSensitivity) {
        if (scrollingSensitivity < 0.01f) scrollingSensitivity = 0.01f;
        if (scrollingSensitivity > 1f) scrollingSensitivity = 1f;
        this.scrollingSensitivity = scrollingSensitivity;
    }

    public void setScrollAction(BiConsumer<Float, Float> scrollAction) {
        this.scrollAction = scrollAction;
    }
}
