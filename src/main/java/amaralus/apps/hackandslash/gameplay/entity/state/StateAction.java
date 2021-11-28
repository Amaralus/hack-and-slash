package amaralus.apps.hackandslash.gameplay.entity.state;

@FunctionalInterface
public interface StateAction {

    void execute(StateContext ctx, long updateTime);
}
