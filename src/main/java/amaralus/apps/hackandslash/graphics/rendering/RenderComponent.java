package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.common.Nullable;
import amaralus.apps.hackandslash.common.Updatable;

public interface RenderComponent extends Updatable, Nullable {

    RenderComponent NULL = new RenderComponent.Null();

    default <C extends RenderComponent> C wrapTo(Class<C> clazz) {
        return clazz.cast(this);
    }

    RenderComponentType getRenderComponentType();

    enum RenderComponentType {
        NULL,
        SPRITE,
        PRIMITIVE,
        FONT
    }

    final class Null implements RenderComponent {

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public void update(long elapsedTime) {
            // do nothing
        }

        @Override
        public RenderComponentType getRenderComponentType() {
            return RenderComponentType.NULL;
        }
    }
}
