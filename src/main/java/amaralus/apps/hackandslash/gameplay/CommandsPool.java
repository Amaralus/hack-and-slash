package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.graphics.entities.sprites.SpriteRenderComponent;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public final class CommandsPool {

    public static final Command<Entity> ENTITY_MOVE_UP = entity -> entity.getPhysicalComponent().move(vec2(0f, -1f));
    public static final Command<Entity> ENTITY_MOVE_DOWN = entity -> entity.getPhysicalComponent().move(vec2(0f, 1f));
    public static final Command<Entity> ENTITY_MOVE_LEFT = entity -> entity.getPhysicalComponent().move(vec2(-1f, 0f));
    public static final Command<Entity> ENTITY_MOVE_RIGHT = entity -> entity.getPhysicalComponent().move(vec2(1f, 0f));

    private CommandsPool() {
    }

    public static Command<Entity> changeFrameStrip(int frameStripNumber) {
        return entity -> entity.getRenderComponent().wrapTo(SpriteRenderComponent.class).changeAnimatedFrameStrip(frameStripNumber);
    }
}
