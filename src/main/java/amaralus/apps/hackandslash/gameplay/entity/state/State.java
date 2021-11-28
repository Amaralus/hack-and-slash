package amaralus.apps.hackandslash.gameplay.entity.state;

import amaralus.apps.hackandslash.common.Updatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class State implements Updatable {

    private static final Logger log = LoggerFactory.getLogger(State.class);

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
    }

    public void removeState() {
        if (!baseState)
            stateSystem.popState();
        else
            log.warn("Попытка удалить базовое состояние [{}]", name);
    }

    public String getName() {
        return name;
    }

    void setBaseState() {
        this.baseState = true;
    }
}
