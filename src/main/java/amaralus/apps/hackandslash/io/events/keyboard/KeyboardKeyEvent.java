package amaralus.apps.hackandslash.io.events.keyboard;

import amaralus.apps.hackandslash.io.events.ButtonEvent;

public class KeyboardKeyEvent extends ButtonEvent<KeyCode> {

    private final int scancode;

    public KeyboardKeyEvent(long window, int key, int scancode, int action, int mods) {
        super(window, key, action, mods);
        this.scancode = scancode;
    }

    @Override
    protected KeyCode toButtonCode(int code) {
        return KeyCode.valueOfNumber(code);
    }

    public int getScancode() {
        return scancode;
    }
}
