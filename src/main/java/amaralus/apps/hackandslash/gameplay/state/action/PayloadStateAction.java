package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.state.StateContext;

@FunctionalInterface
public interface PayloadStateAction<E extends Entity, T> {

    void execute(StateContext<E> stateContext, T payload, long updateTime);
}
