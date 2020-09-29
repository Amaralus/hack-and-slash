package amaralus.apps.hackandslash.io.events;

public class MouseButtonEvent {

    private final long window;
    private final MouseButton button;
    private final int action;
    private final int mods;

    public MouseButtonEvent(long window, int button, int action, int mods) {
        this.window = window;
        this.button = MouseButton.valueOfNumber(button);
        this.action = action;
        this.mods = mods;
    }

    public long getWindow() {
        return window;
    }

    public MouseButton getButton() {
        return button;
    }

    public int getAction() {
        return action;
    }

    public int getMods() {
        return mods;
    }
}
