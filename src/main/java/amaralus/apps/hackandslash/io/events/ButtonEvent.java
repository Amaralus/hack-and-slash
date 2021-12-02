package amaralus.apps.hackandslash.io.events;

public abstract class ButtonEvent<B extends ButtonCode> extends Event {

    private final B buttonCode;
    private final int action;
    private final int mods;

    protected ButtonEvent(long window, int code, int action, int mods) {
        super(window);
        buttonCode = toButtonCode(code);
        this.action = action;
        this.mods = mods;
    }

    protected abstract B toButtonCode(int code);

    public B getButtonCode() {
        return buttonCode;
    }

    public int getAction() {
        return action;
    }

    public int getMods() {
        return mods;
    }
}
