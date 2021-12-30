package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.action.StateAction;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class StateFactory<E extends Entity> {

    private final Map<String, StateAction<E>> states = new HashMap<>();
    private String baseStateName;

    public StateFactory<E> state(String name, StateAction<E> action) {
        states.put(requireNonNull(name, "state name is null!"), requireNonNull(action, "state action is null!"));
        return this;
    }

    public StateFactory<E> baseState(String name, StateAction<E> action) {
        state(name, action);
        baseStateName = name;
        return this;
    }

    public StateSystem<E> produce() {
        if (baseStateName == null)
            throw new IllegalArgumentException("Base state not configured!");

        var stateSystem = new StateSystem<E>();

        stateSystem.setUpStates(states.entrySet()
                .stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> createState(entry.getKey(), entry.getValue(), stateSystem)
                )));

        stateSystem.switchState(baseStateName);

        return stateSystem;
    }

    private State<E> createState(String name, StateAction<E> action, StateSystem<E> stateSystem) {
        return new State<>(name, action, stateSystem);
    }
}
