package amaralus.apps.hackandslash.common.message;

import java.util.function.Consumer;

public class ActionMessageClient extends AbstractMessageClient {

    private final Consumer<Request> action;

    public ActionMessageClient(long id, MessageBroker broker, Consumer<Request> action) {
        super(id, broker);
        this.action = action;
    }

    @Override
    public void receive(Request request) {
        action.accept(request);
    }
}
