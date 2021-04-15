package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.gameplay.UpdateService;
import amaralus.apps.hackandslash.graphics.RendererService;
import amaralus.apps.hackandslash.graphics.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EntityService {

    private static final Logger log = LoggerFactory.getLogger(EntityService.class);

    private UpdateService updateService;
    private final RendererService rendererService;

    private final Set<Entity> allEntities = new HashSet<>();
    private final List<RegisteredInfo> newEntities = new ArrayList<>();

    public EntityService(RendererService rendererService) {
        this.rendererService = rendererService;
    }

    public void registerEntity(Entity entity, Node targetNode, EntityStatus targetStatus) {
        allEntities.add(entity);
        newEntities.add(new RegisteredInfo(entity, targetNode, targetStatus));
        log.debug("Новая сущность id={} зарегестрирована", entity.getEntityId());
    }

    public void activateNewEntities() {
        if (!newEntities.isEmpty()) {
            newEntities.forEach(this::activateEntity);
            newEntities.clear();
        }
    }

    private void activateEntity(RegisteredInfo registeredInfo) {
        var entity = registeredInfo.entity;
        entity.setStatus(registeredInfo.targetStatus);

        updateService.addEntity(entity);

        if (registeredInfo.targetNode == null)
            rendererService.getActiveScene().addChildren(entity);
        else
            registeredInfo.targetNode.addChildren(entity);

        log.debug("Сущность id={} добавлена в обработку", entity.getEntityId());
    }

    @Autowired
    public void setUpdateService(UpdateService updateService) {
        this.updateService = updateService;
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
