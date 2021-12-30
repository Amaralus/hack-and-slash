package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.action.PayloadStateAction;
import amaralus.apps.hackandslash.gameplay.state.action.StateAction;

import java.util.HashMap;
import java.util.Map;

public class MessageState<E extends Entity> extends State<E>{

    private final Map<Class<?>, PayloadStateAction<E, Object>> actionMap = new HashMap<>();

    public MessageState(String name, StateAction<E> action, StateSystem<E> stateSystem) {
        super(name, action, stateSystem);
    }

    @Override
    public void update(long elapsedTime) {
        executeBefore(elapsedTime);

        processMessages(elapsedTime);

        action.execute(context, elapsedTime);

        executeAfter(elapsedTime);
    }

    private void processMessages(long updateTime) {
        var nextMessage = context.messageClient().getNextMessage();
        while (nextMessage.isPresent()) {
            var message = nextMessage.get();
            var action = actionMap.get(message.getClass());
            if (action != null)
                action.execute(context, message, updateTime);
            nextMessage = context.messageClient().getNextMessage();
        }
    }

    public <P> void onMessage(Class<P> payloadClass, PayloadStateAction<E, P> action) {
        actionMap.put(payloadClass, (PayloadStateAction<E, Object>) action);
    }
}
