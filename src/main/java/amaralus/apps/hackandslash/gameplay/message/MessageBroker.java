package amaralus.apps.hackandslash.gameplay.message;

import amaralus.apps.hackandslash.common.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageBroker {

    private static final Logger log = LoggerFactory.getLogger(MessageBroker.class);

    private final TaskManager taskManager;
    private final Map<Long, MessageClient> clientMap = new ConcurrentHashMap<>();

    public MessageBroker(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public MessageClient register(long id) {
        var client = new MessageClient(id, this);
        clientMap.put(id, client);
        log.debug("Клиент id={} зарегистрирован", id);
        return client;
    }

    public void unregister(long id) {
        log.debug("Клиент id={} удален", id);
        clientMap.remove(id);
    }

    public void send(Request request) {
        taskManager.runAsync(() -> sendRequest(request));
    }

    private void sendRequest(Request request) {
        log.trace("Пересылка сообщения {} -> {}", request.sender(), request.receiver());
        clientMap.computeIfPresent(request.receiver(), (id, client) -> {
            client.receive(request);
            return client;
        });
    }
}
