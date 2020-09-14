package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static amaralus.apps.hackandslash.resources.Resource.resourceInfoName;

public class ResourceManager implements Destroyable {

    private static final Logger log = LoggerFactory.getLogger(ResourceManager.class);

    private final Map<Class<? extends Destroyable>, ResourceBundle<? extends Destroyable>> resourceBundleMap;

    public ResourceManager() {
        resourceBundleMap = new HashMap<>();
    }

    @Override
    public void destroy() {
        resourceBundleMap.values().forEach(Destroyable::destroy);
    }

    public <R extends Destroyable> void addResource(String name, R resource) {
        ResourceBundle<R> bundle = (ResourceBundle<R>) getOrCreateResourceBundle(resource.getClass());
        bundle.addResource(name, resource);
        log.debug("Добавлен ресурс {}", resourceInfoName(resource.getClass(), name));
    }

    public <R extends Destroyable> R getResource(String name, Class<R> resourceClass) {
        var bundle = getResourceBundle(resourceClass);
        if (bundle == null)
            throw new ResourceNotFoundException("resource bundle for resource " + resourceInfoName(resourceClass, name) + " doesn`t exist!");
        return bundle.getResource(name).getResourceData();
    }

    private <R extends Destroyable> ResourceBundle<R> getOrCreateResourceBundle(Class<R> resourceClass) {
        var bundle = getResourceBundle(resourceClass);
        if (bundle != null) return bundle;

        bundle = new ResourceBundle<>(resourceClass);
        resourceBundleMap.put(resourceClass, bundle);

        log.debug("Создан контейнер ресурсов для {}", resourceClass.getSimpleName());

        return bundle;
    }

    private <R extends Destroyable> ResourceBundle<R> getResourceBundle(Class<R> resourceClass) {
        return (ResourceBundle<R>) resourceBundleMap.get(resourceClass);
    }
}
