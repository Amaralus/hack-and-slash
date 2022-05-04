package amaralus.apps.hackandslash.demo;

import amaralus.apps.hackandslash.gameplay.entity.EntitySearchService;
import amaralus.apps.hackandslash.gameplay.state.StateContext;
import amaralus.apps.hackandslash.gameplay.state.action.StateAction;
import lombok.Setter;

import static amaralus.apps.hackandslash.common.message.SystemTopic.ENTITY_SEARCH_TOPIC;

public class TargetSearchState implements StateAction<Bot> {

    private boolean requestSent;
    @Setter
    private EntitySearchService.SearchResult searchResult;

    @Override
    public void execute(StateContext<Bot> stateContext, long updateTime) {
        if (requestSent)
            checkResult(stateContext);
        else
            sendRequest(stateContext);
    }

    private void checkResult(StateContext<Bot> stateContext) {
        if (searchResult == null || searchResult.getLiveContext() == null) return;

        stateContext.entity().setTarget(searchResult.getLiveContext());
        stateContext.switchState("movement");
        searchResult = null;
        requestSent = false;
    }

    private void sendRequest(StateContext<Bot> stateContext) {
        var request = new EntitySearchService.SearchRequest(
                stateContext.entity().getPhysicalComponent().getPosition(),
                stateContext.entity().getTargetFilter());

        stateContext.messageClient().send(ENTITY_SEARCH_TOPIC, request);
        requestSent = true;
    }
}
