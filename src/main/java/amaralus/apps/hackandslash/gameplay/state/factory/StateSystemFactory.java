package amaralus.apps.hackandslash.gameplay.state.factory;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.StateSystem;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class StateSystemFactory<E extends Entity> {

    private final Map<String, StateFactory<E>> states = new HashMap<>();
    private String startStateName;

    public StateFactory<E> state(String name) {
        var factory = new StateFactory<>(this, name);
        states.put(requireNonNull(name, "state name is null!"), factory);
        return factory;
    }

    public MessageStateFactory<E> messageState(String name) {
        var factory = new MessageStateFactory<>(this, name);
        states.put(requireNonNull(name, "state name is null!"), factory);
        return factory;
    }

    public StateSystemFactory<E> startState(String name) {
        startStateName = name;
        return this;
    }

    public StateSystem<E> produce() {
        if (startStateName == null)
            throw new IllegalArgumentException("Start state not configured!");

        var stateSystem = new StateSystem<E>();

        stateSystem.setUpStates(states.entrySet()
                .stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().produce(stateSystem)
                )));

        stateSystem.switchState(startStateName);

        return stateSystem;
    }
}
