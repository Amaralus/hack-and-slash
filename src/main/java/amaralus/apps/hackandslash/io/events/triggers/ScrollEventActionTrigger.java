package amaralus.apps.hackandslash.io.events.triggers;

import amaralus.apps.hackandslash.io.events.mouse.ScrollEvent;

import java.util.function.BiConsumer;

public class ScrollEventActionTrigger extends EventActionTrigger<ScrollEvent> {

    private final BiConsumer<Float, Float> action;
    private float scrollingSensitivity = 0.1f;
    private float xOffset;
    private float yOffset;

    public ScrollEventActionTrigger(BiConsumer<Float, Float> action) {
        super(null);
        this.action = action;
        setActive(true);
    }

    @Override
    public void runAction() {
        if (isActive() && (xOffset != 0f || yOffset != 0f)) {
            action.accept(xOffset, yOffset);
            xOffset = 0f;
            yOffset = 0f;
        }
    }

    @Override
    public void handleEvent(ScrollEvent event) {
        xOffset = (float) event.getXOffset() * scrollingSensitivity;
        yOffset = (float) event.getYOffset() * scrollingSensitivity;
    }

    public void setScrollingSensitivity(float scrollingSensitivity) {
        if (scrollingSensitivity < 0.01f) scrollingSensitivity = 0.01f;
        if (scrollingSensitivity > 1f) scrollingSensitivity = 1f;
        this.scrollingSensitivity = scrollingSensitivity;
    }

}
