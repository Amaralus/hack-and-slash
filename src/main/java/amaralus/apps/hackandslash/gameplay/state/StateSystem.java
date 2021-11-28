package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.common.Updatable;

import java.util.*;

public class StateSystem implements Updatable, Destroyable {

    private final Deque<State> stack = new ArrayDeque<>();
    private final Map<String, State> states = new HashMap<>();

    @Override
    public void update(long elapsedTime) {
        currentState().update(elapsedTime);
    }

    @Override
    public void destroy() {
        stack.clear();
        states.clear();
    }

    void setUpStates(Map<String, State> states) {
        this.states.putAll(states);
    }

    State currentState() {
        try {
            return stack.getFirst();
        } catch (NoSuchElementException e) {
            throw new InvalidStateSystemException("Empty state stack!");
        }
    }

    void pushState(String stateName) {
        var state = states.get(stateName);
        if (state == null)
            throw new InvalidStateSystemException("Couldn't switch state! State [" + stateName + "] not found!");

        stack.push(state);
    }

    void popState() {
        try {
            stack.pop();
        } catch (NoSuchElementException e) {
            throw new InvalidStateSystemException("Empty state stack!");
        }
    }
}
