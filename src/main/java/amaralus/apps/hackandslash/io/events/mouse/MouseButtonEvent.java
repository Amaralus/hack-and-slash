package amaralus.apps.hackandslash.io.events.mouse;

import amaralus.apps.hackandslash.io.events.ButtonEvent;

public class MouseButtonEvent extends ButtonEvent<MouseButton> {

    public MouseButtonEvent(long window, int button, int action, int mods) {
        super(window, button, action, mods);
    }

    @Override
    protected MouseButton toButtonCode(int code) {
        return MouseButton.valueOfNumber(code);
    }
}
