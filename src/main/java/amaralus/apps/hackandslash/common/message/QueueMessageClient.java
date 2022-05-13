package amaralus.apps.hackandslash.common.message;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueMessageClient extends AbstractMessageClient {

    private final Queue<Request> messageQueue = new ConcurrentLinkedQueue<>();

    public QueueMessageClient(long id, MessageBroker broker) {
        super(id, broker);
    }

    @Override
    public void destroy() {
        messageQueue.clear();
        super.destroy();
    }

    public Optional<Object> getNextMessage() {
        return Optional.ofNullable(messageQueue.poll())
                .map(Request::payload);
    }

    @Override
    public void receive(Request request) {
        messageQueue.offer(request);
    }

    public void clearMessageQueue() {
        messageQueue.clear();
    }
}
