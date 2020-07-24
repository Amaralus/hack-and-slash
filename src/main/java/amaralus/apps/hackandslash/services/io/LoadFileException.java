package amaralus.apps.hackandslash.services.io;

public class LoadFileException extends RuntimeException {

    public LoadFileException() {
        super();
    }

    public LoadFileException(String message) {
        super(message);
    }

    public LoadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadFileException(Throwable cause) {
        super(cause);
    }

    protected LoadFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
