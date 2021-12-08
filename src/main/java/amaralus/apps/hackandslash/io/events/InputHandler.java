package amaralus.apps.hackandslash.io.events;

import amaralus.apps.hackandslash.common.message.MessageBroker;
import amaralus.apps.hackandslash.common.message.MessageClient;
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

import static amaralus.apps.hackandslash.common.message.SystemTopic.INPUT_TOPIC;
import static amaralus.apps.hackandslash.io.events.InputEventMessage.inputEventMessage;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

@Service
@Slf4j
public class InputHandler {

    private final Map<ButtonCode, ButtonEventActionTrigger> buttonTriggers = new ConcurrentHashMap<>();
    private final MessageClient messageClient;
    private ScrollEventActionTrigger scrollTrigger;

    public InputHandler(Window window, MessageBroker broker) {
        setUpInputHandling(window);
        messageClient = broker.createClient();
        scrollAction(this::sendEvent);
    }

    public InputHandler buttonAction(ButtonCode buttonCode, Runnable action) {
        addTrigger(new ButtonEventActionTrigger(buttonCode, action));
        return this;
    }

    public InputHandler buttonAction(ButtonCode buttonCode) {
        addTrigger(new ButtonEventActionTrigger(buttonCode, () -> sendEvent(buttonCode)));
        return this;
    }

    public InputHandler singleAction(ButtonCode buttonCode, Runnable action) {
        addTrigger(new ButtonEventSingleActionTrigger(buttonCode, action));
        return this;
    }

    public InputHandler singleAction(ButtonCode buttonCode) {
        addTrigger(new ButtonEventSingleActionTrigger(buttonCode, () -> sendEvent(buttonCode)));
        return this;
    }

    public InputHandler scrollAction(BiConsumer<Float, Float> scrollAction) {
        scrollTrigger = new ScrollEventActionTrigger(scrollAction);
        log.debug("Добавлен триггер на скроллинг");
        return this;
    }

    public void handleInputEvents() {
        glfwPollEvents();
        buttonTriggers.values().forEach(EventActionTrigger::runAction);
        if (scrollTrigger != null)
            scrollTrigger.runAction();
    }

    private void addTrigger(ButtonEventActionTrigger buttonTrigger) {
        buttonTriggers.put(buttonTrigger.getButtonCode(), buttonTrigger);
        log.debug("Добавлен триггер на кнопку {}", buttonTrigger.getButtonCode());
    }

    private void sendEvent(float xOffset, float yOffset) {
        sendEvent(inputEventMessage(xOffset, yOffset));
    }

    private void sendEvent(ButtonCode buttonCode) {
        sendEvent(inputEventMessage(buttonCode));
    }

    private void sendEvent(InputEventMessage message) {
        messageClient.send(INPUT_TOPIC, message);
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

    private void handleScrollEvent(ScrollEvent scrollEvent) {
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
