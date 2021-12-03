package amaralus.apps.hackandslash.io.events;

import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.io.events.mouse.ScrollEvent;
import amaralus.apps.hackandslash.io.events.triggers.ButtonEventActionTrigger;
import amaralus.apps.hackandslash.io.events.triggers.ButtonEventSingleActionTrigger;
import amaralus.apps.hackandslash.io.events.triggers.EventActionTrigger;
import amaralus.apps.hackandslash.io.events.triggers.ScrollEventActionTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Service
@Slf4j
public class InputHandler {

    private final Map<ButtonCode, ButtonEventActionTrigger> buttonTriggers = new ConcurrentHashMap<>();
    private ScrollEventActionTrigger scrollTrigger;

    public InputHandler(Window window) {
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
        log.debug("Добавлен триггер на скроллинг");
        return this;
    }

    public void addTrigger(ButtonEventActionTrigger buttonTrigger) {
        buttonTriggers.put(buttonTrigger.getButtonCode(), buttonTrigger);
        log.debug("Добавлен триггер на кнопку {}", buttonTrigger.getButtonCode());
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
        buttonTriggers.computeIfPresent(event.getButtonCode(), (code, trigger) -> {
            trigger.handleEvent(event);
            log.trace("Обработано событие ввода для кнопки {} - {}", event.getButtonCode(), getActionName(event.getAction()));
            return trigger;
        });
    }

    public void handleScrollEvent(ScrollEvent scrollEvent) {
        if (scrollTrigger != null) {
            scrollTrigger.handleEvent(scrollEvent);
            log.trace("Обработано событие скроллинга [{};{}]", scrollEvent.getXOffset(), scrollEvent.getYOffset());
        }
    }

    private String getActionName(int action) {
        switch (action) {
            case 0:
                return "RELEASE";
            case 1:
                return "PRESS";
            case 2:
                return "REPEAT";
            default:
                return "UNKNOWN";
        }
    }
}
