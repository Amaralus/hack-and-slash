package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.common.message.QueueMessageClient;
import amaralus.apps.hackandslash.gameplay.entity.Entity;

public class StateContext<E extends Entity> {

    private final State<E> state;

    public StateContext(State<E> state) {
        this.state = state;
    }

    public void switchState(String stateName) {
        state.switchState(stateName);
    }

    public String stateName() {
        return state.getName();
    }

    public E entity() {
        return state.getStateSystem().getEntity();
    }

    public QueueMessageClient messageClient() {
        return entity().getMessageClient();
    }
}
