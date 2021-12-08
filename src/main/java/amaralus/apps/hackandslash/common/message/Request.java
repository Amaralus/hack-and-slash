package amaralus.apps.hackandslash.common.message;

public class Request {

    private final long sender;
    private final Object payload;

    public Request(long sender, Object payload) {
        this.sender = sender;
        this.payload = payload;
    }

    public long sender() {
        return sender;
    }

    public Object payload() {
        return payload;
    }
}
