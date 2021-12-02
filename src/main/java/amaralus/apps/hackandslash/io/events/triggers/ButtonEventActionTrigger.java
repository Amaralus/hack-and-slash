package amaralus.apps.hackandslash.io.events.triggers;

import amaralus.apps.hackandslash.io.events.ButtonCode;
import amaralus.apps.hackandslash.io.events.ButtonEvent;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class ButtonEventActionTrigger extends EventActionTrigger<ButtonEvent<?>> {

    private final ButtonCode buttonCode;

    public ButtonEventActionTrigger(ButtonCode buttonCode, Runnable action) {
        super(action);
        this.buttonCode = buttonCode;
    }

    @Override
    public void handleEvent(ButtonEvent<?> event) {
        if (GLFW_PRESS == event.getAction())
            setActive(true);
        if (GLFW_RELEASE == event.getAction())
            setActive(false);
    }

    public ButtonCode getButtonCode() {
        return buttonCode;
    }
}
