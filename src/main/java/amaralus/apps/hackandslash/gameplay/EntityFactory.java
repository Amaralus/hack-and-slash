package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.graphics.entities.RenderComponent;
import amaralus.apps.hackandslash.graphics.entities.sprites.Sprite;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

@Component
public class EntityFactory {

    private final ResourceManager resourceManager;

    private String spriteName;
    private Vector2f position;
    private float speed = 100f;

    public EntityFactory(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public Entity produce() {
        var renderComponent = new RenderComponent(getSprite(spriteName));
        var entity = new Entity(renderComponent, position);
        entity.setSpeedPerSec(speed);
        return entity;
    }

    public EntityFactory sprite(String spriteName) {
        this.spriteName = spriteName;
        return this;
    }

    public EntityFactory position(Vector2f position) {
        this.position = position;
        return this;
    }

    public EntityFactory position(float x, float y) {
        return position(vec2(x, y));
    }

    public EntityFactory speed(float speed) {
        this.speed = speed;
        return this;
    }

    private Sprite getSprite(String spriteName) {
        return resourceManager.getResource(spriteName, Sprite.class);
    }
}
