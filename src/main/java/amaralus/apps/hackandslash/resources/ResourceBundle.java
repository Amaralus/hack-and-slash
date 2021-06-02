package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.common.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static amaralus.apps.hackandslash.resources.Resource.resourceInfoName;

public final class ResourceBundle<R extends Resource> implements Destroyable {

    private static final Logger log = LoggerFactory.getLogger(ResourceBundle.class);

    private final Map<String, R> resourceMap;
    private final Class<R> resourcesClass;

    ResourceBundle(Class<R> resourcesClass) {
        this.resourcesClass = resourcesClass;
        resourceMap = new HashMap<>();
    }

    public void addResource(R resource) {
        if (resourceMap.putIfAbsent(resource.getResourceName(), resource) != null) {
            resource.destroy();
            throw new IllegalArgumentException("resource " + resource.resourceInfoName() + " already exist!");
        }
    }

    public R getResource(String name) {
        var resource = resourceMap.get(name);
        if (resource == null) throw new ResourceNotFoundException("resource " + resourceInfoName(resourcesClass, name) + " not found!");
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

    public Class<R> getResourcesClass() {
        return resourcesClass;
    }

}
