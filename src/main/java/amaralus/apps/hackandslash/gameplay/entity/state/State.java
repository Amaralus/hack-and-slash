package amaralus.apps.hackandslash.gameplay.entity.state;

import amaralus.apps.hackandslash.common.Updatable;

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
    }

    public void removeState() {
        // todo warn log
        if (!baseState)
            stateSystem.popState();
    }

    public String getName() {
        return name;
    }

    void setBaseState() {
        this.baseState = true;
    }
}
