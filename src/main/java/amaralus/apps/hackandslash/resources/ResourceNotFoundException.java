package amaralus.apps.hackandslash.resources;

import static amaralus.apps.hackandslash.resources.Resource.resourceInfoName;

public final class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> clazz, String name) {
        this("resource " + resourceInfoName(clazz, name) + " not found!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
