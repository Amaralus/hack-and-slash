package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.common.Destroyable;

public class Resource<R extends Destroyable> implements Destroyable {

    private final R resourceData;
    private final String name;

    public Resource(String name, R resourceData) {
        this.resourceData = resourceData;
        this.name = name;
    }

    public static String resourceInfoName(Class<?> clazz, String name) {
        return clazz.getSimpleName() + "#" + name;
    }

    @Override
    public void destroy() {
        resourceData.destroy();
    }

    public R getResourceData() {
        return resourceData;
    }

    public String getName() {
        return name;
    }
}
