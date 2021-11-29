package amaralus.apps.hackandslash.gameplay.message;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class Topic {

    private final String name;
    private final MessageBroker broker;
    private final Set<Long> subscribers = new CopyOnWriteArraySet<>();

    public Topic(String name, MessageBroker broker) {
        this.name = name;
        this.broker = broker;
    }

    public void receive(Request request) {
        subscribers.forEach(clientId -> broker.sendToClient(clientId, name, request));
    }

    public void subscribe(long clientId) {
        subscribers.add(clientId);
    }

    public void unsubscribe(long clientId) {
        subscribers.remove(clientId);
    }

    public String getName() {
        return name;
    }

    public Set<Long> getSubscribers() {
        return subscribers;
    }
}
