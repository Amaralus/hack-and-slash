package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.state.StateContext;

import java.util.HashMap;
import java.util.Map;

public class MessageProcessingStateAction implements StateAction {

    private final Map<Class<?>, PayloadStateAction<Object>> actionMap = new HashMap<>();

    @Override
    public void execute(StateContext stateContext, long updateTime) {
        var nextMessage = stateContext.messageClient().getNextMessage();
        while (nextMessage.isPresent()) {
            var message = nextMessage.get();
            var action = actionMap.get(message.getClass());
            if (action != null)
                action.execute(stateContext, message, updateTime);
            nextMessage = stateContext.messageClient().getNextMessage();
        }
    }

    public <P> MessageProcessingStateAction onMessage(Class<P> payloadClass, PayloadStateAction<P> action) {
        actionMap.put(payloadClass, (PayloadStateAction<Object>) action);
        return this;
    }
}
