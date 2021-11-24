package amaralus.apps.hackandslash.gameplay.message;

import amaralus.apps.hackandslash.common.TaskManager;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageBroker {

    private final TaskManager taskManager;
    private final Map<Long, MessageClient> clientMap = new ConcurrentHashMap<>();

    public MessageBroker(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public MessageClient register(long id) {
        var client = new MessageClient(id, this);
        clientMap.put(id, client);
        return client;
    }

    public void unregister(long id) {
        clientMap.remove(id);
    }

    public void send(Request request) {
        taskManager.runAsync(() -> clientMap.computeIfPresent(request.clientId(), (id, client) -> {
            client.receive(request);
            return client;
        }));
    }
}
