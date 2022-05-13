package amaralus.apps.hackandslash.demo;

import amaralus.apps.hackandslash.gameplay.entity.EntitySearchService;
import amaralus.apps.hackandslash.gameplay.state.StateSystem;
import amaralus.apps.hackandslash.gameplay.state.factory.StateSystemFactory;

public class BotFactory {

    private StateSystem<Bot> produceStateSystem() {

        var targetSearch = new TargetSearchState();

        return new StateSystemFactory<Bot>()
                .messageState("target-search")
                .onMessage(EntitySearchService.SearchResult.class, (ctx, result, uploadTime) -> targetSearch.setSearchResult(result))
                .action(targetSearch)
                .state("movement")
                .action(new MovementState())
                .state("transform-target")
                .action(new TransformTargetState())
//                .state("transformation")
//                .action()
                .produce();
    }
}
