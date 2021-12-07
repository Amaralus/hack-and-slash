package amaralus.apps.hackandslash.gameplay.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageProcessingStateAction implements StateAction {

    private final Map<Class<?>, TriAction<Object>> actionMap = new HashMap<>();

    @Override
    public void execute(StateContext stateContext, long updateTime) {
        Optional<Object> nextMessage = stateContext.messageClient().getNextMessage();
        while (nextMessage.isPresent()) {
            var message = nextMessage.get();
            var action = actionMap.get(message.getClass());
            if (action != null)
                action.execute(message, stateContext, updateTime);
            nextMessage = stateContext.messageClient().getNextMessage();
        }
    }

    public <P> void addAction(Class<P> clazz, TriAction<P> action) {
        actionMap.put(clazz, (TriAction<Object>) action);
    }

    public interface TriAction<P> {

        void execute(P payload, StateContext stateContext, long updateTime);
    }
}
