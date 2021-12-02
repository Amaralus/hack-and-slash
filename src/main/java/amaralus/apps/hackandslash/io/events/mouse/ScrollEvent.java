package amaralus.apps.hackandslash.io.events.mouse;

import amaralus.apps.hackandslash.io.events.Event;

public class ScrollEvent extends Event {

    private final double xOffset;
    private final double yOffset;

    public ScrollEvent(long window, double xOffset, double yOffset) {
        super(window);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public double getXOffset() {
        return xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }
}
