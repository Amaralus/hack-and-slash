package amaralus.apps.hackandslash.demo;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityContext;
import lombok.Getter;

@Getter
public class BotContext extends EntityContext {

    private final BotType botType;

    protected BotContext(Entity entity) {
        super(entity);
        botType = ((Bot) entity).getBotType();
    }
}
