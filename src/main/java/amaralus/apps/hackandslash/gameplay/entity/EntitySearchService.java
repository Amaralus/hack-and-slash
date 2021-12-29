package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.common.message.MessageBroker;
import amaralus.apps.hackandslash.common.message.MessageClient;
import amaralus.apps.hackandslash.common.message.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector2f;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.common.message.SystemTopic.ENTITY_SEARCH_TOPIC;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;

// todo -> возможно станет общим сервисом поиска
@Service
public class EntitySearchService {

    private final EntityService entityService;
    private final MessageClient client;

    public EntitySearchService(EntityService entityService, MessageBroker broker) {
        this.entityService = entityService;
        client = broker.createActionClient(this::findEntity);
        client.subscribe(ENTITY_SEARCH_TOPIC);
    }

    private void findEntity(Request request) {
        var position = (Vector2f) request.payload();
        var entity = getClosestEntity(position);
        client.send(request.sender(), new SearchResult(entity == null ? null : entity.getLiveContext()));
    }

    private Entity getClosestEntity(Vector2f position) {
        float minSquaredDistance = Float.MAX_VALUE;
        Entity resultEntity = null;

        for (var entity : entityService.getAllEntities()) {
            float sqDist = position.distanceSquared(entity.getPhysicalComponent().getPosition());
            if (entity.getStatus() != REMOVE && sqDist < minSquaredDistance) {
                resultEntity = entity;
                minSquaredDistance = sqDist;
            }
        }

        return resultEntity;
    }

    @AllArgsConstructor
    @Getter
    public static class SearchResult {
        private final EntityLiveContext liveContext;
    }
}
