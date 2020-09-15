package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static amaralus.apps.hackandslash.resources.Resource.resourceInfoName;

public final class ResourceBundle<R extends Destroyable> implements Destroyable {

    private static final Logger log = LoggerFactory.getLogger(ResourceBundle.class);

    private final Map<String, Resource<R>> resourceMap;
    private final Class<R> resourcesClass;

    ResourceBundle(Class<R> resourcesClass) {
        this.resourcesClass = resourcesClass;
        resourceMap = new HashMap<>();
    }

    public void addResource(String name, R resource) {
        addResource(new Resource<>(name, resource));
    }

    public void addResource(Resource<R> resource) {
        if (resourceMap.putIfAbsent(resource.getName(), resource) != null) {
            resource.destroy();
            throw new IllegalArgumentException("resource " + resourceInfoName(resourcesClass, resource.getName()) + " already exist!");
        }
    }

    public Resource<R> getResource(String name) {
        var resource = resourceMap.get(name);
        if (resource == null) throw new ResourceNotFoundException("resource " + resourceInfoName(resourcesClass, name) + " not found!");
        return resource;
    }

    @Override
    public void destroy() {
        for (Resource<R> resource : resourceMap.values()) {
            resource.destroy();
            log.debug("Ресурс освобождён: {}", resourceInfoName(resourcesClass, resource.getName()));
        }
    }

    public Class<R> getResourcesClass() {
        return resourcesClass;
    }

}
