package amaralus.apps.hackandslash.gameplay.state;

import amaralus.apps.hackandslash.common.Updatable;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.action.StateAction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class State<E extends Entity> implements Updatable {

    private final String name;
    private final StateSystem<E> stateSystem;
    protected final StateContext<E> context;

    protected final StateAction<E> action;
    private StateAction<E> beforeAction;
    private StateAction<E> afterAction;

    public State(String name, StateAction<E> action, StateSystem<E> stateSystem) {
        this.name = name;
        this.stateSystem = stateSystem;
        this.action = action;
        context = new StateContext<>(this);
    }

    @Override
    public void update(long elapsedTime) {
        executeBefore(elapsedTime);

        action.execute(context, elapsedTime);

        executeAfter(elapsedTime);
    }

    protected void executeBefore(long elapsedTime) {
        if (beforeAction != null)
            beforeAction.execute(context, elapsedTime);
    }

    protected void executeAfter(long elapsedTime) {
        if (afterAction != null)
            afterAction.execute(context, elapsedTime);
    }

    public void switchState(String stateName) {
        stateSystem.switchState(stateName);
        log.trace("Переключение состояния [{}] -> [{}] сущность id={}", name, stateName, stateSystem.getEntity().getEntityId());
    }

    void setBeforeAction(StateAction<E> beforeAction) {
        this.beforeAction = beforeAction;
    }

    void setAfterAction(StateAction<E> afterAction) {
        this.afterAction = afterAction;
    }

    public String getName() {
        return name;
    }

    StateSystem<E> getStateSystem() {
        return stateSystem;
    }
}
