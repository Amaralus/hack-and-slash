package amaralus.apps.hackandslash.io.events.triggers;

import amaralus.apps.hackandslash.io.events.ButtonCode;
import amaralus.apps.hackandslash.io.events.ButtonEvent;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class ButtonEventSingleActionTrigger extends ButtonEventActionTrigger {

    private boolean actionExecuted;

    public ButtonEventSingleActionTrigger(ButtonCode buttonCode, Runnable action) {
        super(buttonCode, action);
    }

    @Override
    public void runAction() {
        if (isActive() && !actionExecuted) {
            super.runAction();
            actionExecuted = true;
        }
    }

    @Override
    public void handleEvent(ButtonEvent<?> event) {
        super.handleEvent(event);
        if (GLFW_RELEASE == event.getAction())
            actionExecuted = false;
    }
}
