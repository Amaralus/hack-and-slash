package amaralus.apps.hackandslash.gameplay.state;

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

    public String getName() {
        return state.getName();
    }
}
