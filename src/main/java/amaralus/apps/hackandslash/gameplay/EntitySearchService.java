package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.common.message.MessageBroker;
import amaralus.apps.hackandslash.common.message.MessageClient;
import amaralus.apps.hackandslash.common.message.Request;
import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityContext;
import amaralus.apps.hackandslash.gameplay.entity.EntityService;
import org.joml.Vector2f;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.common.message.SystemTopic.ENTITY_SEARCH_TOPIC;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;

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
        var entityContext = getClosestEntity(position);
        client.send(request.sender(), entityContext);
    }

    private EntityContext getClosestEntity(Vector2f position) {
        float minSquaredDistance = Float.MAX_VALUE;
        Entity resultEntity = null;

        for (var entity : entityService.getAllEntities()) {
            float sqDist = position.distanceSquared(entity.getGlobalPosition());
            if (entity.getStatus() != REMOVE && sqDist < minSquaredDistance) {
                resultEntity = entity;
                minSquaredDistance = sqDist;
            }
        }

        return resultEntity == null ? null : resultEntity.getEntityContext();
    }
}
