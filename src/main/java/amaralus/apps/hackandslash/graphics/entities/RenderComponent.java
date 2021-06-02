package amaralus.apps.hackandslash.graphics.entities;

import amaralus.apps.hackandslash.common.Nullable;
import amaralus.apps.hackandslash.common.Updateable;

public interface RenderComponent extends Updateable, Nullable {

    default <C extends RenderComponent> C wrapTo(Class<C> clazz) {
        return clazz.cast(this);
    }

    RenderComponentType getRenderComponentType();

    enum RenderComponentType {
        NULL,
        SPRITE,
        PRIMITIVE
    }

    final class NullRenderComponent implements RenderComponent {

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
