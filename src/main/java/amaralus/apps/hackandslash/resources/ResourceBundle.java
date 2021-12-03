package amaralus.apps.hackandslash.resources;

import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.common.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
class ResourceBundle<R extends Resource> implements Destroyable, Nullable {

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
        resourceMap.values().forEach(this::removeResource);
    }

    public Class<R> getResourcesClass() {
        return resourcesClass;
    }

    static final class NullResourceBundle extends ResourceBundle {

        public NullResourceBundle() {
            super(null);
        }

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public void addResource(Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeResource(Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Resource getResource(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void destroy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class getResourcesClass() {
            throw new UnsupportedOperationException();
        }
    }
}
