package amaralus.apps.hackandslash.common.message;

import java.util.HashSet;
import java.util.Set;

abstract class AbstractMessageClient implements MessageClient {

    protected final long id;
    protected final MessageBroker broker;
    protected final Set<String> topics = new HashSet<>();

    protected AbstractMessageClient(long id, MessageBroker broker) {
        this.id = id;
        this.broker = broker;
    }

    @Override
    public void destroy() {
        topics.forEach(topic -> broker.unsubscribe(topic, id));
        broker.deleteClient(id);
    }

    @Override
    public void send(long receiver, Object payload) {
        broker.send(receiver, new Request(id, payload));
    }

    @Override
    public void send(String topicName, Object payload) {
        broker.send(topicName, new Request(id, payload));
    }

    @Override
    public void send(SystemTopic systemTopic, Object payload) {
        send(systemTopic.getName(), payload);
    }

    @Override
    public void subscribe(String topicName) {
        broker.subscribe(topicName, id);
        topics.add(topicName);
    }

    @Override
    public void subscribe(SystemTopic systemTopic) {
        subscribe(systemTopic.getName());
    }

    @Override
    public void unsubscribe(String topicName) {
        broker.unsubscribe(topicName, id);
        topics.remove(topicName);
    }

    @Override
    public void unsubscribe(SystemTopic systemTopic) {
        unsubscribe(systemTopic.getName());
    }

    void removeSubscription(String topicName) {
        topics.remove(topicName);
    }

    @Override
    public long getId() {
        return id;
    }
}
