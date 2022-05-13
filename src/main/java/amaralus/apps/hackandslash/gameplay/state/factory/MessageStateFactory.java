package amaralus.apps.hackandslash.gameplay.state.factory;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.MessageState;
import amaralus.apps.hackandslash.gameplay.state.State;
import amaralus.apps.hackandslash.gameplay.state.StateSystem;
import amaralus.apps.hackandslash.gameplay.state.action.PayloadStateAction;

import java.util.HashMap;
import java.util.Map;

public class MessageStateFactory<E extends Entity> extends StateFactory<E> {

    private final Map<Class<?>, PayloadStateAction<E, Object>> actions = new HashMap<>();

    MessageStateFactory(StateSystemFactory<E> stateSystemFactory, String stateName) {
        super(stateSystemFactory, stateName);
    }

    @Override
    State<E> produce(StateSystem<E> stateSystem) {
        var state = super.produce(stateSystem);
        ((MessageState<E>) state).setUpActions(actions);
        return state;
    }

    public <P> MessageStateFactory<E> onMessage(Class<P> payloadClass, PayloadStateAction<E, P> action) {
        actions.put(payloadClass, (PayloadStateAction<E, Object>) action);
        return this;
    }
}
