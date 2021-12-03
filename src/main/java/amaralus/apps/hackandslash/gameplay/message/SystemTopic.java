package amaralus.apps.hackandslash.gameplay.message;

public enum SystemTopic {
    INPUT_TOPIC("input");

    private final String name;

    SystemTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
