package amaralus.apps.hackandslash.common.message;

import amaralus.apps.hackandslash.common.Destroyable;

public interface MessageClient extends Destroyable {

    long getId();

    void send(long receiver, Object payload);

    void send(String topicName, Object payload);

    void send(SystemTopic systemTopic, Object payload);

    void receive(Request request);

    void subscribe(String topicName);

    void subscribe(SystemTopic systemTopic);

    void unsubscribe(String topicName);

    void unsubscribe(SystemTopic systemTopic);
}
