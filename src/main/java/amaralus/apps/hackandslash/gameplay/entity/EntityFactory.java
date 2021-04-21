package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import amaralus.apps.hackandslash.graphics.entities.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.graphics.scene.Node;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

@Component
public class EntityFactory {

    private final ResourceManager resourceManager;
    private final EntityService entityService;

    public EntityFactory(ResourceManager resourceManager, EntityService entityService) {
        this.resourceManager = resourceManager;
        this.entityService = entityService;
    }

    public EntityBuilder newEntity() {
        return new EntityBuilder();
    }

    public class EntityBuilder {

        private RenderComponent renderComponent;
        private Vector2f startPosition;
        private RemovingStrategy removingStrategy;
        private float movementSpeed;
        private Node targetNode;
        private EntityStatus startEntityStatus;

        public Entity produce() {
            var entity = new Entity(renderComponent, startPosition, removingStrategy);
            entity.setSpeedPerSec(movementSpeed);
            return  entity;
        }

        public Entity register() {
            var entity = produce();
            entityService.registerEntity(entity, targetNode, startEntityStatus);
            return entity;
        }

        public EntityBuilder renderComponent(RenderComponent renderComponent) {
            this.renderComponent = renderComponent;
            return this;
        }

        public EntityBuilder sprite(String spriteName) {
            return renderComponent(new SpriteRenderComponent(getSprite(spriteName)));
        }

        public EntityBuilder startPosition(float x, float y) {
            return startPosition(vec2(x, y));
        }

        public EntityBuilder startPosition(Vector2f startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public EntityBuilder removingStrategy(RemovingStrategy removingStrategy) {
            this.removingStrategy = removingStrategy;
            return this;
        }

        public EntityBuilder movementSpeed(float movementSpeed) {
            this.movementSpeed = movementSpeed;
            return this;
        }

        public EntityBuilder targetNode(Node targetNode) {
            this.targetNode = targetNode;
            return this;
        }

        public EntityBuilder startEntityStatus(EntityStatus startEntityStatus) {
            this.startEntityStatus = startEntityStatus;
            return this;
        }

        private Sprite getSprite(String spriteName) {
            return resourceManager.getResource(spriteName, Sprite.class);
        }
    }
}
