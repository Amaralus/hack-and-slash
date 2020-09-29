package amaralus.apps.hackandslash.io.events;

public class ScrollEvent {

    private final long window;
    private final double xOffset;
    private final double yOffset;

    public ScrollEvent(long window, double xOffset, double yOffset) {
        this.window = window;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public long getWindow() {
        return window;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }
}
