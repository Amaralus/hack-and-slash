package amaralus.apps.hackandslash.gameplay;

public interface Command<O> {

    void execute(O object);
}
