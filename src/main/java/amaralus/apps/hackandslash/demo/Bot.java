package amaralus.apps.hackandslash.demo;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityContext;
import amaralus.apps.hackandslash.gameplay.entity.EntityLiveContext;
import amaralus.apps.hackandslash.gameplay.entity.EntityStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class Bot extends Entity {

    private EntityLiveContext target;
    private float transformDistance = 15;
    private BotType botType;

    public boolean isTargetAvailable() {
        return target.getStatus() != EntityStatus.REMOVE;
    }

    public Predicate<Entity> getTargetFilter() {
        return null;
    }

    @Override
    protected EntityContext createContext() {
        return new BotContext(this);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
