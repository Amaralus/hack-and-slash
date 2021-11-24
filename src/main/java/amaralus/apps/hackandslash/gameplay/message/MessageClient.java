package amaralus.apps.hackandslash.gameplay.message;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageClient {

    private final long id;
    private final MessageBroker broker;
    private final Queue<Request> messageQueue = new ConcurrentLinkedQueue<>();

    public MessageClient(long id, MessageBroker broker) {
        this.id = id;
        this.broker = broker;
    }

    public Optional<Object> getNextMessage() {
        return Optional.ofNullable(messageQueue.poll())
                .map(Request::getPayload);
    }

    public void send(long id, Object payload) {
        broker.send(new Request(id, payload));
    }

    public void receive(Request request) {
        messageQueue.offer(request);
    }

    public long getId() {
        return id;
    }
}
