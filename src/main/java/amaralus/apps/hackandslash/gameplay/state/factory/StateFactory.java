package amaralus.apps.hackandslash.gameplay.state.factory;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.State;
import amaralus.apps.hackandslash.gameplay.state.StateSystem;
import amaralus.apps.hackandslash.gameplay.state.action.StateAction;

import static java.util.Objects.requireNonNull;

public class StateFactory<E extends Entity> {

    private final StateSystemFactory<E> stateSystemFactory;
    private final String stateName;

    private StateAction<E> before;
    private StateAction<E> action;
    private StateAction<E> after;

    StateFactory(StateSystemFactory<E> stateSystemFactory, String stateName) {
        this.stateSystemFactory = stateSystemFactory;
        this.stateName = stateName;
    }

    State<E> produce(StateSystem<E> stateSystem) {
        var state = new State<>(stateName, requireNonNull(action, "Action not configured for state " + stateName), stateSystem);
        state.setBeforeAction(before);
        state.setAfterAction(after);
        return state;
    }

    public StateSystemFactory<E> done() {
        return stateSystemFactory;
    }

    public StateFactory<E> before(StateAction<E> before) {
        this.before = before;

        return this;
    }

    public StateFactory<E> action(StateAction<E> action) {
        this.action = action;
        return this;
    }

    public StateFactory<E> after(StateAction<E> after) {
        this.after = after;
        return this;
    }
}
