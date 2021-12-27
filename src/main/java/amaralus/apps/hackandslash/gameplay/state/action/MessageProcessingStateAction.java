package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.state.StateContext;

import java.util.HashMap;
import java.util.Map;

public class MessageProcessingStateAction implements StateAction {

    private final Map<Class<?>, PayloadStateAction<Object>> actionMap = new HashMap<>();
    private StateAction beforeExecute;
    private StateAction afterExecute;

    @Override
    public void execute(StateContext stateContext, long updateTime) {
        if (beforeExecute != null) beforeExecute.execute(stateContext, updateTime);

        var nextMessage = stateContext.messageClient().getNextMessage();
        while (nextMessage.isPresent()) {
            var message = nextMessage.get();
            var action = actionMap.get(message.getClass());
            if (action != null)
                action.execute(stateContext, message, updateTime);
            nextMessage = stateContext.messageClient().getNextMessage();
        }

        if (afterExecute != null) afterExecute.execute(stateContext, updateTime);
    }

    public <P> MessageProcessingStateAction onMessage(Class<P> payloadClass, PayloadStateAction<P> action) {
        actionMap.put(payloadClass, (PayloadStateAction<Object>) action);
        return this;
    }

    public MessageProcessingStateAction beforeExecute(StateAction beforeExecute) {
        checkThis(beforeExecute);
        this.beforeExecute = beforeExecute;
        return this;
    }

    public MessageProcessingStateAction afterExecute(StateAction afterExecute) {
        checkThis(afterExecute);
        this.afterExecute = afterExecute;
        return this;
    }

    private void checkThis(StateAction stateAction) {
        if (stateAction instanceof MessageProcessingStateAction)
            throw new UnsupportedOperationException("State action MessageProcessingStateAction is unsupported in MessageProcessingStateAction");
    }
}
