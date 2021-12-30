package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.StateContext;

@FunctionalInterface
public interface StateAction<E extends Entity> {

    void execute(StateContext<E> stateContext, long updateTime);
}
