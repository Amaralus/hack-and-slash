package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.message.MessageClient;

public class StateContext {

    private final State state;

    public StateContext(State state) {
        this.state = state;
    }

    public void switchState(String stateName) {
        state.switchState(stateName);
    }

    public void removeState() {
        state.removeState();
    }

    public String stateName() {
        return state.getName();
    }

    public Entity entity() {
        return state.getStateSystem().getEntity();
    }

    public MessageClient messageClient() {
        return entity().getMessageClient();
    }
}
