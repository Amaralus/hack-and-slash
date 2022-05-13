package amaralus.apps.hackandslash.resources;

public class ResourceAlreadyExistException extends RuntimeException {

    public ResourceAlreadyExistException(Resource<?> resource) {
        super("Ресурс " + resource.infoName() + " уже существует!");
    }
}
