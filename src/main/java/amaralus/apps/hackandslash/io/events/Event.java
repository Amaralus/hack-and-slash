package amaralus.apps.hackandslash.io.events;

public abstract class Event {

    private final long window;

    public Event(long window) {
        this.window = window;
    }

    public long getWindow() {
        return window;
    }
}
