package amaralus.apps.hackandslash.gameplay.message;

import amaralus.apps.hackandslash.common.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MessageBroker {

    private static final Logger log = LoggerFactory.getLogger(MessageBroker.class);
    private static final AtomicLong clientIdSource = new AtomicLong();

    private final TaskManager taskManager;
    private final Map<Long, MessageClient> clients = new ConcurrentHashMap<>();
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();

    public MessageBroker(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public MessageClient createClient() {
        long id = clientIdSource.incrementAndGet();
        var client = new MessageClient(id, this);
        clients.put(id, client);
        log.debug("Клиент id={} зарегистрирован", id);
        return client;
    }

    void deleteClient(long id) {
        clients.remove(id);
        log.debug("Клиент id={} удален", id);
    }

    public void createTopic(String topicName) {
        topics.put(topicName, new Topic(topicName, this));
        log.debug("Создан топик {}", topicName);
    }

    public void deleteTopic(String topicName) {
        topics.remove(topicName).getSubscribers().stream()
                .map(clients::get)
                .forEach(client -> {
                    client.removeSubscription(topicName);
                    log.debug("Клиент id={} отписан от топика {}", client.getId(), topicName);
                });

        log.debug("Удален топик {}", topicName);
    }

    void subscribe(String topicName, long clientId) {
        var topic = topics.get(topicName);

        if (topic == null) {
            log.warn("Топик с именем [{}] не существует! Подписчик id={}", topicName, clientId);
            return;
        }

        topic.subscribe(clientId);
        log.debug("Клиент id={} подписан на топик {}", clientId, topicName);
    }

    void unsubscribe(String topicName, long clientId) {
        var topic = topics.get(topicName);

        if (topic == null) {
            log.warn("Топик с именем [{}] не существует! Подписчик id={}", topicName, clientId);
            return;
        }

        topic.unsubscribe(clientId);
        log.debug("Клиент id={} отписан от топика {}", clientId, topicName);
    }

    void send(long receiver, Request request) {
        taskManager.runAsync(() -> sendToClient(receiver, request.sender(), request));
    }

    void send(String topicName, Request request) {
        taskManager.runAsync(() -> sendToTopic(topicName, request));
    }

    void sendToClient(long receiver, Object sender, Request request) {
        var client = clients.get(receiver);
        if (client == null)
            log.warn("Клиент id={} не зарегистрирован! Отправитель id={}", receiver, sender);
        else {
            log.trace("Пересылка сообщения {} -> {}", sender, receiver);
            client.receive(request);
        }
    }

    private void sendToTopic(String topicName, Request request) {
        var topic = topics.get(topicName);
        if (topic == null)
            log.warn("Топик с именем [{}] не существует! Отправитель id={}", topicName, request.sender());
        else {
            log.trace("Пересылка сообщения в топик {} -> {}", request.sender(), topicName);
            topic.receive(request);
        }
    }
}
