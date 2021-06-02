package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.common.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static amaralus.apps.hackandslash.resources.Resource.resourceInfoName;

@Service
public class ResourceManager implements Destroyable {

    private static final Logger log = LoggerFactory.getLogger(ResourceManager.class);

    private final Map<Class<? extends Destroyable>, ResourceBundle<? extends Destroyable>> resourceBundleMap;

    public ResourceManager() {
        resourceBundleMap = new ConcurrentHashMap<>();
    }

    @Override
    public void destroy() {
        resourceBundleMap.values().forEach(Destroyable::destroy);
    }

    public <R extends Resource> void addResource(R resource) {
        ResourceBundle<R> bundle = (ResourceBundle<R>) getOrCreateResourceBundle(resource.getClass());
        bundle.addResource(resource);
        var resourceInfo = resource.resourceInfoName();
        log.debug("Добавлен ресурс {}", resourceInfo);
    }

    public <R extends Resource> R getResource(String name, Class<R> resourceClass) {
        var bundle = getResourceBundle(resourceClass);
        if (bundle == null)
            throw new ResourceNotFoundException("resource bundle for resource " + resourceInfoName(resourceClass, name) + " doesn`t exist!");
        return bundle.getResource(name);
    }

    private <R extends Resource> ResourceBundle<R> getOrCreateResourceBundle(Class<R> resourceClass) {
        var bundle = getResourceBundle(resourceClass);
        if (bundle != null) return bundle;

        bundle = new ResourceBundle<>(resourceClass);
        resourceBundleMap.put(resourceClass, bundle);

        log.debug("Создан контейнер ресурсов для {}", resourceClass.getSimpleName());

        return bundle;
    }

    private <R extends Resource> ResourceBundle<R> getResourceBundle(Class<R> resourceClass) {
        return (ResourceBundle<R>) resourceBundleMap.get(resourceClass);
    }
}
