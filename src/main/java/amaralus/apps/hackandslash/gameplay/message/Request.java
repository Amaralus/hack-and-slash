package amaralus.apps.hackandslash.gameplay.message;

public class Request {

    private final long sender;
    private final long receiver;
    private final Object payload;

    public Request(long sender, long receiver, Object payload) {
        this.sender = sender;
        this.receiver = receiver;
        this.payload = payload;
    }

    public long sender() {
        return sender;
    }

    public long receiver() {
        return receiver;
    }

    public Object payload() {
        return payload;
    }
}
