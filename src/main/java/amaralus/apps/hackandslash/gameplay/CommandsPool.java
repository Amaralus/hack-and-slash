package amaralus.apps.hackandslash.gameplay;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public final class CommandsPool {

    public static final Command<Entity> ENTITY_MOVE_UP = entity -> entity.move(vec2(0f, -1f));
    public static final Command<Entity> ENTITY_MOVE_DOWN = entity -> entity.move(vec2(0f, 1f));
    public static final Command<Entity> ENTITY_MOVE_LEFT = entity -> entity.move(vec2(-1f, 0f));
    public static final Command<Entity> ENTITY_MOVE_RIGHT = entity -> entity.move(vec2(1f, 0f));

    private CommandsPool() {
    }
}
