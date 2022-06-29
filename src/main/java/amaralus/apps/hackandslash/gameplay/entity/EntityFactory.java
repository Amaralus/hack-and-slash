package amaralus.apps.hackandslash.gameplay.entity;

import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.rendering.RenderComponent;
import amaralus.apps.hackandslash.graphics.sprites.Animation;
import amaralus.apps.hackandslash.graphics.sprites.Sprite;
import amaralus.apps.hackandslash.graphics.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.graphics.sprites.repository.SpriteRepository;
import amaralus.apps.hackandslash.resources.ResourceFactory;
import amaralus.apps.hackandslash.scene.Node;
import amaralus.apps.hackandslash.scene.NodeRemovingStrategy;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.UPDATING;
import static amaralus.apps.hackandslash.scene.NodeRemovingStrategy.SINGLE;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

@Component
@RequiredArgsConstructor
public class EntityFactory {

    private final SpriteRepository spriteRepository;
    private final ResourceFactory resourceFactory;
    private final EntityService entityService;

    public EntityBuilder entity() {
        return new EntityBuilder();
    }

    public SpriteRenderComponentBuilder spriteRenderComponent() {
        return new SpriteRenderComponentBuilder();
    }

    public PrimitiveRenderComponentBuilder primitiveRenderComponent() {
        return new PrimitiveRenderComponentBuilder();
    }

    public class EntityBuilder {

        private RenderComponent renderComponent = RenderComponent.NULL;
        private Vector2f startPosition = vec2();
        private NodeRemovingStrategy nodeRemovingStrategy = SINGLE;
        private float movementSpeed = 100f;
        private Node targetNode;
        private EntityStatus entityStatus = UPDATING;

        public Entity produce() {
            var entity = new Entity();
            entity.getPhysicalComponent().setPosition(startPosition);
            entity.getPhysicalComponent().setSpeed(movementSpeed);
            entity.setRemovingStrategy(nodeRemovingStrategy);
            entity.setRenderComponent(renderComponent);
            return entity;
        }

        public Entity register() {
            var entity = produce();
            entityService.registerEntity(entity, targetNode, entityStatus);
            return entity;
        }

        public EntityBuilder renderComponent(RenderComponent renderComponent) {
            this.renderComponent = renderComponent;
            return this;
        }

        public EntityBuilder position(float x, float y) {
            return position(vec2(x, y));
        }

        public EntityBuilder position(Vector2f startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public EntityBuilder removingStrategy(NodeRemovingStrategy nodeRemovingStrategy) {
            this.nodeRemovingStrategy = nodeRemovingStrategy;
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

        public EntityBuilder entityStatus(EntityStatus startEntityStatus) {
            this.entityStatus = startEntityStatus;
            return this;
        }
    }

    public class SpriteRenderComponentBuilder {

        private String spriteName;
        private int frameStrip = 0;
        private boolean runAnimation = false;

        public RenderComponent produce() {
            var renderComponent = new SpriteRenderComponent(getSprite(spriteName));
            renderComponent.setCurrentFrameStrip(frameStrip);
            if (runAnimation)
                renderComponent.computeAnimation(Animation::start);
            return renderComponent;
        }

        public SpriteRenderComponentBuilder spriteName(String spriteName) {
            this.spriteName = spriteName;
            return this;
        }

        public SpriteRenderComponentBuilder frameStrip(int frameStrip) {
            this.frameStrip = frameStrip;
            return this;
        }

        public SpriteRenderComponentBuilder runAnimation() {
            runAnimation = true;
            return this;
        }

        private Sprite getSprite(String spriteName) {
            return spriteRepository.get(spriteName);
        }
    }

    public class PrimitiveRenderComponentBuilder {

        private String primitiveName;
        private Color color;
        private boolean isLine;

        private final Vector2f[] linePoints = new Vector2f[2];
        private final Vector2f[] trianglePoints = new Vector2f[3];

        public RenderComponent produce() {
            return isLine ?
                    resourceFactory.produceLine(primitiveName, color, linePoints[0], linePoints[1])
                    : resourceFactory.produceTriangle(primitiveName, color, trianglePoints[0], trianglePoints[1], trianglePoints[2]);
        }

        public PrimitiveRenderComponentBuilder primitiveName(String primitiveName) {
            this.primitiveName = primitiveName;
            return this;
        }

        public PrimitiveRenderComponentBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public PrimitiveRenderComponentBuilder line(Vector2f start, Vector2f end) {
            isLine = true;
            linePoints[0] = start;
            linePoints[1] = end;
            return this;
        }

        public PrimitiveRenderComponentBuilder triangle(Vector2f topPoint, Vector2f bottomRightPoint, Vector2f bottomLeftPoint) {
            isLine = false;
            trianglePoints[0] = topPoint;
            trianglePoints[1] = bottomRightPoint;
            trianglePoints[2] = bottomLeftPoint;
            return this;
        }
    }
}
