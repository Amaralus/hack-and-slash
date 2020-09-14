package amaralus.apps.hackandslash.services;

import java.util.HashMap;
import java.util.Map;

public final class ServiceLocator {

    private static final ServiceLocator INSTANCE = new ServiceLocator();

    private final Map<Class<?>, Object> services = new HashMap<>();

    private ServiceLocator() {
    }

    public static ServiceLocator serviceLocator() {
        return INSTANCE;
    }

    public static void registerService(Object service) {
        INSTANCE.register(service);
    }

    public static <S> S getService(Class<S> serviceClass) {
        return INSTANCE.get(serviceClass);
    }

    public void register(Object service) {
        services.put(service.getClass(), service);
    }

    public <S> S get(Class<S> serviceClass) {
        return serviceClass.cast(services.get(serviceClass));
    }
}
