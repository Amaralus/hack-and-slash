package amaralus.apps.hackandslash.gameplay.state.action;

import amaralus.apps.hackandslash.gameplay.state.StateContext;

@FunctionalInterface
public interface StateAction {

    void execute(StateContext stateContext, long updateTime);
}
