package amaralus.apps.hackandslash.gameplay.state;

@FunctionalInterface
public interface StateAction {

    void execute(StateContext stateContext, long updateTime);
}
