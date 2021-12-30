package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.StateContext;
import amaralus.apps.hackandslash.io.events.ButtonCode;
import amaralus.apps.hackandslash.io.events.InputEventMessage;

import java.util.HashMap;
import java.util.Map;

public class InputEventProcessingAction<E extends Entity> implements PayloadStateAction<E, InputEventMessage> {

    private final Map<ButtonCode, StateAction<E>> buttonsActions = new HashMap<>();
    private ScrollAction<E> scrollAction;

    @Override
    public void execute(StateContext<E> stateContext, InputEventMessage inputMessage, long updateTime) {
        if (inputMessage.isButtonEvent()) {
            var action = buttonsActions.get(inputMessage.getButtonCode());
            if (action != null)
                action.execute(stateContext, updateTime);
        } else if (scrollAction != null)
            scrollAction.execute(stateContext, inputMessage.getXOffset(), inputMessage.getYOffset(), updateTime);
    }

    public InputEventProcessingAction<E> onButton(ButtonCode buttonCode, StateAction<E> stateAction) {
        buttonsActions.put(buttonCode, stateAction);
        return this;
    }

    public InputEventProcessingAction<E> onScroll(ScrollAction<E> scrollAction) {
        this.scrollAction = scrollAction;
        return this;
    }

    @FunctionalInterface
    public interface ScrollAction<E extends Entity> {

        void execute(StateContext<E> stateContext, float xOffset, float yOffset, long updateTime);
    }
}
