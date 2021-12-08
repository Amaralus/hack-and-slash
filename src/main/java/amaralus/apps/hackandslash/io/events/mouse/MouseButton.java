package amaralus.apps.hackandslash.io.events.mouse;

import amaralus.apps.hackandslash.io.events.ButtonCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum MouseButton implements ButtonCode {

    UNKNOWN(-1),
    MOUSE_BUTTON_LEFT(0),
    MOUSE_BUTTON_RIGHT(1),
    MOUSE_BUTTON_MIDDLE(2),
    MOUSE_BUTTON_4(3),
    MOUSE_BUTTON_5(4),
    MOUSE_BUTTON_6(5),
    MOUSE_BUTTON_7(6),
    MOUSE_BUTTON_8(7);

    private static final Map<Integer, MouseButton> BY_NUMBER = new ConcurrentHashMap<>();

    static {
        for (var mouseButton : values()) BY_NUMBER.put(mouseButton.number, mouseButton);
    }

    private final int number;

    MouseButton(int number) {
        this.number = number;
    }

    public static MouseButton valueOfNumber(int number) {
        var mouseButton = BY_NUMBER.get(number);
        return mouseButton == null ? UNKNOWN : mouseButton;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
