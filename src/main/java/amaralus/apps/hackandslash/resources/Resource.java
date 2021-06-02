package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.common.Destroyable;

import java.util.Objects;

public abstract class Resource implements Destroyable {

    private final String resourceName;

    public Resource(String resourceName) {
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public static String resourceInfoName(Class<?> clazz, String name) {
        return clazz.getSimpleName() + "#" + name;
    }

    public String resourceInfoName() {
        return resourceInfoName(getClass(), resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }
}
