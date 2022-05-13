package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.StateContext;

@FunctionalInterface
public interface PayloadStateAction<E extends Entity, P> {

    void execute(StateContext<E> stateContext, P payload, long updateTime);
}
