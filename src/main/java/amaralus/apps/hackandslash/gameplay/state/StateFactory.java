package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.gameplay.state.action.StateAction;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class StateFactory {

    private final Map<String, StateAction> states = new HashMap<>();
    private String baseStateName;

    public StateFactory state(String name, StateAction action) {
        states.put(requireNonNull(name, "state name is null!"), requireNonNull(action, "state action is null!"));
        return this;
    }

    public StateFactory baseState(String name, StateAction action) {
        state(name, action);
        baseStateName = name;
        return this;
    }

    public StateSystem produce() {
        if (baseStateName == null)
            throw new IllegalArgumentException("Base state not configured!");

        var stateSystem = new StateSystem();

        stateSystem.setUpStates(states.entrySet()
                .stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> createState(entry.getKey(), entry.getValue(), stateSystem)
                )));

        stateSystem.pushState(baseStateName);

        return stateSystem;
    }

    private State createState(String name, StateAction action, StateSystem stateSystem) {
        var state = new State(name, action, stateSystem);

        if (name.equals(baseStateName)) {
            state.setBaseState();
        }

        return state;
    }
}
