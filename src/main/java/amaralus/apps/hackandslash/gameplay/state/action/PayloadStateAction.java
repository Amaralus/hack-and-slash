package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.state.StateContext;

@FunctionalInterface
public interface PayloadStateAction<T> {

    void execute(StateContext stateContext, T payload, long updateTime);
}
