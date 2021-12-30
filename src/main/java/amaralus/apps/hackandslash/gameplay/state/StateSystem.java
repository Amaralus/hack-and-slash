package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.common.Updatable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class StateSystem<E extends Entity> implements Updatable, Destroyable {

    private final Map<String, State<E>> states = new HashMap<>();
    private E entity;
    private State<E> currentState;

    @Override
    public void destroy() {
        states.clear();
        entity = null;
    }

    @Override
    public void update(long elapsedTime) {
        currentState.update(elapsedTime);
    }

    public void switchState(String stateName) {
        var state = states.get(stateName);
        if (state == null)
            throw new InvalidStateSystemException("Couldn't switch state! State [" + stateName + "] not found!");
        currentState = state;
    }

    public void setUpStates(Map<String, State<E>> states) {
        this.states.putAll(states);
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public State<E> currentState() {
        return currentState;
    }
}
