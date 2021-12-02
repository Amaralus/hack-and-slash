package amaralus.apps.hackandslash.io.events;

import amaralus.apps.hackandslash.gameplay.message.MessageBroker;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.io.events.mouse.ScrollEvent;
import amaralus.apps.hackandslash.io.events.triggers.ButtonEventActionTrigger;
import amaralus.apps.hackandslash.io.events.triggers.ButtonEventSingleActionTrigger;
import amaralus.apps.hackandslash.io.events.triggers.EventActionTrigger;
import amaralus.apps.hackandslash.io.events.triggers.ScrollEventActionTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Service
public class InputHandler {

    public static final String INPUT_TOPIC_NAME = "input";

    private final Map<ButtonCode, ButtonEventActionTrigger> buttonTriggers = new ConcurrentHashMap<>();
    private ScrollEventActionTrigger scrollTrigger;

    public InputHandler(Window window, MessageBroker messageBroker) {
        messageBroker.createTopic(INPUT_TOPIC_NAME);

        setUpInputHandling(window);
    }

    public InputHandler buttonAction(ButtonCode buttonCode, Runnable action) {
        addTrigger(new ButtonEventActionTrigger(buttonCode, action));
        return this;
    }

    public InputHandler singleAction(ButtonCode buttonCode, Runnable action) {
        addTrigger(new ButtonEventSingleActionTrigger(buttonCode, action));
        return this;
    }

    public InputHandler scrollAction(BiConsumer<Float, Float> scrollAction) {
        scrollTrigger = new ScrollEventActionTrigger(scrollAction);
        return this;
    }

    public void addTrigger(ButtonEventActionTrigger buttonTrigger) {
        buttonTriggers.put(buttonTrigger.getButtonCode(), buttonTrigger);
    }

    public void executeActions() {
        buttonTriggers.values().forEach(EventActionTrigger::runAction);
        if (scrollTrigger != null)
            scrollTrigger.runAction();
    }

    private void setUpInputHandling(Window window) {
        window.setKeyCallback(this::handleButtonEvent);
        window.setMouseButtonCallback(this::handleButtonEvent);
        window.setScrollCallback(this::handleScrollEvent);
    }

    private void handleButtonEvent(ButtonEvent<?> event) {
        System.out.println(event.getButtonCode());
        buttonTriggers.get(event.getButtonCode()).handleEvent(event);
    }

    public void handleScrollEvent(ScrollEvent scrollEvent) {
        if (scrollTrigger != null)
            scrollTrigger.handleEvent(scrollEvent);
    }
}
