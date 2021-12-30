package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.StateContext;

import java.util.HashMap;
import java.util.Map;

public class MessageProcessingStateAction<E extends Entity> implements StateAction<E> {

    private final Map<Class<?>, PayloadStateAction<E, Object>> actionMap = new HashMap<>();
    private StateAction<E> beforeExecute;
    private StateAction<E> afterExecute;

    @Override
    public void execute(StateContext<E> stateContext, long updateTime) {
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

    public <P> MessageProcessingStateAction<E> onMessage(Class<P> payloadClass, PayloadStateAction<E, P> action) {
        actionMap.put(payloadClass, (PayloadStateAction<E, Object>) action);
        return this;
    }

    public MessageProcessingStateAction<E> beforeExecute(StateAction<E> beforeExecute) {
        checkThis(beforeExecute);
        this.beforeExecute = beforeExecute;
        return this;
    }

    public MessageProcessingStateAction<E> afterExecute(StateAction<E> afterExecute) {
        checkThis(afterExecute);
        this.afterExecute = afterExecute;
        return this;
    }

    private void checkThis(StateAction<E> stateAction) {
        if (stateAction instanceof MessageProcessingStateAction)
            throw new UnsupportedOperationException("State action MessageProcessingStateAction is unsupported in MessageProcessingStateAction");
    }
}
