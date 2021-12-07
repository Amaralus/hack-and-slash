package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.common.Updatable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class State implements Updatable {

    private final String name;
    private final StateSystem stateSystem;
    private final StateContext context;
    private final StateAction action;
    private boolean baseState;

    public State(String name, StateAction action, StateSystem stateSystem) {
        this.name = name;
        this.stateSystem = stateSystem;
        this.action = action;
        context = new StateContext(this);
    }

    @Override
    public void update(long elapsedTime) {
        action.execute(context, elapsedTime);
    }

    public void switchState(String stateName) {
        stateSystem.pushState(stateName);
        logStateSwitching(name, stateName);
    }

    public void removeState() {
        if (!baseState) {
            stateSystem.popState();
            logStateSwitching(name, stateSystem.currentState().name);
        } else
            log.warn("Попытка удалить базовое состояние [{}]", name);
    }

    public String getName() {
        return name;
    }

    void setBaseState() {
        this.baseState = true;
    }

    StateSystem getStateSystem() {
        return stateSystem;
    }

    private void logStateSwitching(String prevState, String newState) {
        log.trace("Переключение состояния [{}] -> [{}]", prevState, newState);
    }
}
