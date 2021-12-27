package amaralus.apps.hackandslash.common.message;

public enum SystemTopic {
    INPUT_TOPIC("input"),
    ENTITY_SEARCH_TOPIC("entity-search");

    private final String name;

    SystemTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
