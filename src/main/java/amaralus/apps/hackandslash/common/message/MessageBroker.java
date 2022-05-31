package amaralus.apps.hackandslash.common.message;

import amaralus.apps.hackandslash.reactive.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Service
@Slf4j
public class MessageBroker {

    private static final AtomicLong clientIdSource = new AtomicLong();

    private final TaskManager taskManager;
    private final Map<Long, MessageClient> clients = new ConcurrentHashMap<>();
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();

    public MessageBroker(TaskManager taskManager) {
        this.taskManager = taskManager;
        for (var systemTopic : SystemTopic.values())
            createTopic(systemTopic.getName());
    }

    public QueueMessageClient createQueueClient() {
        var client = new QueueMessageClient(clientIdSource.incrementAndGet(), this);
        registerClient(client);
        return client;
    }

    public ActionMessageClient createActionClient(Consumer<Request> action) {
        Consumer<Request>  asyncAction = request -> taskManager.runAsync(() ->  action.accept(request));
        var client  = new ActionMessageClient(clientIdSource.incrementAndGet() ,this, asyncAction);
        registerClient(client);
        return client;
    }

    private void registerClient(MessageClient client) {
        clients.put(client.getId(), client);
        log.debug("Клиент id={} зарегистрирован", client.getId());
    }

    void deleteClient(long id) {
        clients.remove(id);
        log.debug("Клиент id={} удален", id);
    }

    public void createTopic(String topicName) {
        topics.computeIfAbsent(topicName, name -> {
            log.debug("Создан топик {}", topicName);
            return new Topic(name, this);
        });
    }

    public void deleteTopic(String topicName) {
        var topic = topics.remove(topicName);

        if (topic == null) {
            log.warn("Попытка удалить несуществующий топик [{}]", topicName);
            return;
        }

        topic.getSubscribers().stream()
                .map(clients::get)
                .forEach(client -> {
                    ((ActionMessageClient) client).removeSubscription(topicName);
                    log.debug("Клиент id={} отписан от топика {}", client.getId(), topicName);
                });

        log.debug("Удален топик {}", topicName);
    }

    void subscribe(String topicName, long clientId) {
        var topic = topics.get(topicName);

        if (topic == null) {
            log.warn("Топик [{}] не существует! Подписчик id={}", topicName, clientId);
            return;
        }

        topic.subscribe(clientId);
        log.debug("Клиент id={} подписан на топик [{}]", clientId, topicName);
    }

    void unsubscribe(String topicName, long clientId) {
        var topic = topics.get(topicName);

        if (topic == null) {
            log.warn("Топик [{}] не существует! Подписчик id={}", topicName, clientId);
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
            log.warn("Топик [{}] не существует! Отправитель id={}", topicName, request.sender());
        else {
            log.trace("Пересылка сообщения в топик {} -> {}", request.sender(), topicName);
            topic.receive(request);
        }
    }
}
