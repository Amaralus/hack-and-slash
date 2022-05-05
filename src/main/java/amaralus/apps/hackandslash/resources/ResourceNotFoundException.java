package amaralus.apps.hackandslash.resources;

public final class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> clazz, Object id) {
        this("resource " + clazz.getSimpleName() + "#" + id + " not found!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
