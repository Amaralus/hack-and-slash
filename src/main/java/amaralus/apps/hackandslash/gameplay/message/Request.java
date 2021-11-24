package amaralus.apps.hackandslash.gameplay.message;

public class Request {

    private final long id;
    private final Object payload;

    public Request(long id, Object payload) {
        this.id = id;
        this.payload = payload;
    }

    public long clientId() {
        return id;
    }

    public Object getPayload() {
        return payload;
    }
}
