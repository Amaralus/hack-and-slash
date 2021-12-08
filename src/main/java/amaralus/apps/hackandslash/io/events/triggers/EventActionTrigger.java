package amaralus.apps.hackandslash.io.events.triggers;

import amaralus.apps.hackandslash.io.events.Event;

public abstract class EventActionTrigger<E extends Event> {

    private final Runnable action;
    private boolean active;

    protected EventActionTrigger(Runnable action) {
        this.action = action;
    }

    public void runAction() {
        if (isActive())
            action.run();
    }

    public abstract void handleEvent(E event);

    protected void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
