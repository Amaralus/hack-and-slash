package amaralus.apps.hackandslash.graphics.entities;

import amaralus.apps.hackandslash.common.Updateable;

public interface RenderComponent extends Updateable {

    default <C extends RenderComponent> C wrapTo(Class<C> clazz) {
        return clazz.cast(this);
    }
}
