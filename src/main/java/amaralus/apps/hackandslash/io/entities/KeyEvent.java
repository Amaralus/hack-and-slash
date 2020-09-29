package amaralus.apps.hackandslash.io.entities;

public class KeyEvent {

    private final long window;
    private final KeyCode key;
    private final int scancode; 
    private final int action; 
    private final int mods;

    public KeyEvent(long window, int key, int scancode, int action, int mods) {
        this.window = window;
        this.key = KeyCode.valueOfNumber(key);
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }

    public long getWindow() {
        return window;
    }

    public KeyCode getKey() {
        return key;
    }

    public int getScancode() {
        return scancode;
    }

    public int getAction() {
        return action;
    }

    public int getMods() {
        return mods;
    }
}
