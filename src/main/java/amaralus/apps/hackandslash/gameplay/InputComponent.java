package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.gameplay.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class InputComponent {

    private final List<Command<Entity>> commands = new ArrayList<>();

    public void addCommand(Command<Entity> command) {
        commands.add(command);
    }

    public void executeCommands(Entity entity) {
        for (var command : commands)
            command.execute(entity);
        commands.clear();
    }
}
