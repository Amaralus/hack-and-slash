package amaralus.apps.hackandslash.io.events;

import lombok.Getter;

@Getter
public class InputEventMessage {

    private final ButtonCode buttonCode;
    private final float xOffset;
    private final float yOffset;
    private final boolean buttonEvent;

    public static InputEventMessage inputEventMessage(ButtonCode buttonCode) {
        return new InputEventMessage(buttonCode);
    }

    public static InputEventMessage inputEventMessage(float xOffset, float yOffset) {
        return new InputEventMessage(xOffset, yOffset);
    }

    private InputEventMessage(ButtonCode buttonCode) {
        this.buttonCode = buttonCode;
        xOffset = 0f;
        yOffset = 0f;
        buttonEvent = true;
    }

    private InputEventMessage(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        buttonCode = null;
        buttonEvent = false;
    }

    public boolean isScrollEvent() {
        return !isButtonEvent();
    }
}
