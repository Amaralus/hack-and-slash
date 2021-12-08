package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.gameplay.UpdateService;
import amaralus.apps.hackandslash.gameplay.message.MessageBroker;
import amaralus.apps.hackandslash.graphics.RendererService;
import amaralus.apps.hackandslash.graphics.scene.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static amaralus.apps.hackandslash.graphics.scene.NodeRemovingStrategy.SINGLE;

@Service
@Slf4j
public class EntityService {

    private UpdateService updateService;
    private final RendererService rendererService;
    private final MessageBroker messageBroker;

    private final Set<Entity> allEntities = new HashSet<>();
    private final List<RegisteredInfo> newEntities = new ArrayList<>();

    public EntityService(RendererService rendererService, MessageBroker messageBroker) {
        this.rendererService = rendererService;
        this.messageBroker = messageBroker;
    }

    public void registerEntity(Entity entity, Node targetNode, EntityStatus targetStatus) {
        entity.setMessageClient(messageBroker.createClient());
        allEntities.add(entity);
        newEntities.add(new RegisteredInfo(entity, targetNode, targetStatus));
        log.debug("Новая сущность id={} зарегистрирована, clientId={}", entity.getEntityId(), entity.getMessageClient().getId());
    }

    public void activateNewEntities() {
        if (!newEntities.isEmpty()) {
            updateService.addEntities(newEntities.stream()
                    .map(this::activateEntity)
                    .collect(Collectors.toList()));
            newEntities.clear();
        }
    }

    public void removeAllEntities() {
        updateService.removeAll();
        removeEntities(allEntities);
    }

    public void removeEntities(Collection<Entity> entities) {
        for (var entity : entities) {
            if (entity.getRemovingStrategy() == SINGLE) {
                entity.getParent().addChildren(entity.getChildren().toArray(Node[]::new));
            }

            entity.getParent().getChildren().remove(entity);
            entity.setParent(null);
            entity.getChildren().clear();
            entity.getMessageClient().destroy();
            if (entity.getStateSystem() != null)
                entity.getStateSystem().destroy();

            log.debug("Удалена сущность id={}", entity.getEntityId());
        }

        allEntities.removeAll(entities);
    }

    private Entity activateEntity(RegisteredInfo registeredInfo) {
        var entity = registeredInfo.entity;
        entity.setStatus(registeredInfo.targetStatus);

        if (registeredInfo.targetNode == null)
            rendererService.getActiveScene().addChildren(entity);
        else
            registeredInfo.targetNode.addChildren(entity);

        log.debug("Сущность id={} добавлена в обработку", entity.getEntityId());
        return entity;
    }

    @Autowired
    public void setUpdateService(UpdateService updateService) {
        this.updateService = updateService;
    }

    public Set<Entity> getAllEntities() {
        return Collections.unmodifiableSet(allEntities);
    }

    private static class RegisteredInfo {
        Entity entity;
        Node targetNode;

        EntityStatus targetStatus;

        public RegisteredInfo(Entity entity, Node targetNode, EntityStatus targetStatus) {
            this.entity = entity;
            this.targetNode = targetNode;
            this.targetStatus = targetStatus;
        }
    }
}
