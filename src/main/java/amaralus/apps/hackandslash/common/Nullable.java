package amaralus.apps.hackandslash.common;

public interface Nullable {

    default boolean isNull() {
        return false;
    }

    default boolean notNull() {
        return !isNull();
    }
}
