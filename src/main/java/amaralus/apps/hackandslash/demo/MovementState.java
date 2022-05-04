package amaralus.apps.hackandslash.demo;

import amaralus.apps.hackandslash.gameplay.state.StateContext;
import amaralus.apps.hackandslash.gameplay.state.action.StateAction;

public class MovementState implements StateAction<Bot> {

    @Override
    public void execute(StateContext<Bot> stateContext, long updateTime) {
        var bot = stateContext.entity();
        var physicalComponent = bot.getPhysicalComponent();

        if (!bot.isTargetAvailable()) {
            bot.setTarget(null);
            stateContext.switchState("target-search");
        }

        var targetPosition = bot.getTarget().getGlobalPosition();
        physicalComponent.setTargetPosition(targetPosition);
        physicalComponent.update(updateTime);

        if (physicalComponent.getPosition().distanceSquared(targetPosition) < bot.getTransformDistance() * bot.getTransformDistance())
            stateContext.switchState("transform-target");
    }
}
