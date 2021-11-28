package amaralus.apps.hackandslash.gameplay.entity.state;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.common.Updatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StateSystem implements Updatable, Destroyable {

    private static final Logger log = LoggerFactory.getLogger(StateSystem.class);

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

        logStateSwitching(currentState().getName(), stateName);
        stack.push(state);
    }

    void popState() {
        try {
            var prevState = stack.pop();
            logStateSwitching(prevState.getName(), currentState().getName());
        } catch (NoSuchElementException e) {
            throw new InvalidStateSystemException("Empty state stack!");
        }
    }

    private void logStateSwitching(String prevState, String newState) {
        log.trace("Переключение состояния [{}] -> [{}]", prevState, newState);
    }
}
