package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.common.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ResourceBundle<R extends Resource> implements Destroyable {

    private static final Logger log = LoggerFactory.getLogger(ResourceBundle.class);

    private final Map<String, R> resourceMap;
    private final Class<R> resourcesClass;

    ResourceBundle(Class<R> resourcesClass) {
        this.resourcesClass = resourcesClass;
        resourceMap = new ConcurrentHashMap<>();
    }

    public void addResource(R resource) {
        if (resourceMap.putIfAbsent(resource.getResourceName(), resource) != null) {
            resource.destroy();
            throw new IllegalArgumentException("resource " + resource.resourceInfoName() + " already exist!");
        }
    }

    public void removeResource(R resource) {
        resourceMap.remove(resource.getResourceName());
        resource.destroy();
        var resourceInfo = resource.resourceInfoName();
        log.debug("Ресурс освобождён: {}", resourceInfo);
    }

    public R getResource(String name) {
        var resource = resourceMap.get(name);
        if (resource == null) throw new ResourceNotFoundException(resourcesClass, name);
        return resource;
    }

    @Override
    public void destroy() {
        for (var resource : resourceMap.values()) {
            resource.destroy();
            var resourceInfo = resource.resourceInfoName();
            log.debug("Ресурс освобождён: {}", resourceInfo);
        }
    }

    public boolean isEmpty() {
        return resourceMap.isEmpty();
    }

    public Class<R> getResourcesClass() {
        return resourcesClass;
    }

}
