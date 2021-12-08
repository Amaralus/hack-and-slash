package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.state.StateContext;
import amaralus.apps.hackandslash.io.events.ButtonCode;
import amaralus.apps.hackandslash.io.events.InputEventMessage;

import java.util.HashMap;
import java.util.Map;

public class InputEventProcessingAction implements PayloadStateAction<InputEventMessage> {

    private final Map<ButtonCode, StateAction> buttonsActions = new HashMap<>();
    private ScrollAction scrollAction;

    @Override
    public void execute(StateContext stateContext, InputEventMessage inputMessage, long updateTime) {
        if (inputMessage.isButtonEvent()) {
            var action = buttonsActions.get(inputMessage.getButtonCode());
            if (action != null)
                action.execute(stateContext, updateTime);
        } else if (scrollAction != null)
            scrollAction.execute(stateContext, inputMessage.getXOffset(), inputMessage.getYOffset(), updateTime);
    }

    public InputEventProcessingAction onButton(ButtonCode buttonCode, StateAction stateAction) {
        buttonsActions.put(buttonCode, stateAction);
        return this;
    }

    public InputEventProcessingAction onScroll(ScrollAction scrollAction) {
        this.scrollAction = scrollAction;
        return this;
    }

    @FunctionalInterface
    public interface ScrollAction {

        void execute(StateContext stateContext, float xOffset, float yOffset, long updateTime);
    }
}
