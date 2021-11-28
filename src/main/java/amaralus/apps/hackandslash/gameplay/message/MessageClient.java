package amaralus.apps.hackandslash.gameplay.message;

import amaralus.apps.hackandslash.common.Destroyable;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageClient implements Destroyable {

    private final long id;
    private final MessageBroker broker;
    private final Queue<Request> messageQueue = new ConcurrentLinkedQueue<>();

    public MessageClient(long id, MessageBroker broker) {
        this.id = id;
        this.broker = broker;
    }

    @Override
    public void destroy() {
        messageQueue.clear();
        broker.unregister(id);
    }

    public Optional<Object> getNextMessage() {
        return Optional.ofNullable(messageQueue.poll())
                .map(Request::payload);
    }

    public void send(long receiver, Object payload) {
        broker.send(new Request(id, receiver, payload));
    }

    public void receive(Request request) {
        messageQueue.offer(request);
    }

    public long getId() {
        return id;
    }
}
