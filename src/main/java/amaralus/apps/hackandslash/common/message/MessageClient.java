package amaralus.apps.hackandslash.common.message;

import amaralus.apps.hackandslash.common.Destroyable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageClient implements Destroyable {

    private final long id;
    private final MessageBroker broker;
    private final Queue<Request> messageQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> topics = new HashSet<>();

    public MessageClient(long id, MessageBroker broker) {
        this.id = id;
        this.broker = broker;
    }

    @Override
    public void destroy() {
        messageQueue.clear();
        topics.forEach(topic -> broker.unsubscribe(topic, id));
        broker.deleteClient(id);
    }

    public Optional<Object> getNextMessage() {
        return Optional.ofNullable(messageQueue.poll())
                .map(Request::payload);
    }

    public void send(long receiver, Object payload) {
        broker.send(receiver, new Request(id, payload));
    }

    public void send(String topicName, Object payload) {
        broker.send(topicName, new Request(id, payload));
    }

    public void send(SystemTopic systemTopic, Object payload) {
        send(systemTopic.getName(), payload);
    }

    public void receive(Request request) {
        messageQueue.offer(request);
    }

    public void subscribe(String topicName) {
        broker.subscribe(topicName, id);
        topics.add(topicName);
    }

    public void subscribe(SystemTopic systemTopic) {
        subscribe(systemTopic.getName());
    }

    public void unsubscribe(String topicName) {
        broker.unsubscribe(topicName, id);
        topics.remove(topicName);
    }

    public void unsubscribe(SystemTopic systemTopic) {
        unsubscribe(systemTopic.getName());
    }

    void removeSubscription(String topicName) {
        topics.remove(topicName);
    }

    public long getId() {
        return id;
    }
}
