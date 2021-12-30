package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.common.Updatable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.action.StateAction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class State<E extends Entity> implements Updatable {

    private final String name;
    private final StateSystem<E> stateSystem;
    private final StateContext<E> context;
    private final StateAction<E> action;

    public State(String name, StateAction<E> action, StateSystem<E> stateSystem) {
        this.name = name;
        this.stateSystem = stateSystem;
        this.action = action;
        context = new StateContext<>(this);
    }

    @Override
    public void update(long elapsedTime) {
        action.execute(context, elapsedTime);
    }

    public void switchState(String stateName) {
        stateSystem.switchState(stateName);
        log.trace("Переключение состояния [{}] -> [{}]", name, stateName);
    }

    public String getName() {
        return name;
    }

    StateSystem<E> getStateSystem() {
        return stateSystem;
    }
}
