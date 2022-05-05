package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.common.Destroyable;

public interface Resource<I extends Comparable<I>> extends Destroyable {

    I getResourceId();

    default String infoName() {
        return getClass().getSimpleName() + "#" + getResourceId();
    }
}
