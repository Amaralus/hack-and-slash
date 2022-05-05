package amaralus.apps.hackandslash.resources.repository;

import amaralus.apps.hackandslash.common.Configurer;
import amaralus.apps.hackandslash.common.Destroyable;
import amaralus.apps.hackandslash.resources.Resource;
import amaralus.apps.hackandslash.resources.ResourceAlreadyExistException;
import amaralus.apps.hackandslash.resources.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentSkipListMap;

import static amaralus.apps.hackandslash.resources.repository.DeletePolicy.REMOVE_AND_DESTROY;

@Slf4j
public class InMemoryResourceRepository<R extends Resource<I>, I extends Comparable<I>>
        implements ResourceRepository<R, I>,
        Configurer<RepositoryConfiguration>,
        Destroyable {

    private final ConcurrentSkipListMap<I, R> resources = new ConcurrentSkipListMap<>();
    private final RepositoryConfiguration configuration = new RepositoryConfiguration();
    private final Class<?> resourceClass;

    protected InMemoryResourceRepository(Class<?> resourceClass) {
        this.resourceClass = resourceClass;
        configure(configuration);
    }

    @Override
    public final R save(R resource) {
        R previous = resources.put(resource.getResourceId(), resource);

        if (previous != null)
            applyRewritePolicy(previous);
        else
            log.debug("Добавлен ресурс {}", resource.infoName());

        return resource;
    }

    @Override
    public void delete(I id) {
        delete(id, configuration.getDeletePolicy());
    }

    @Override
    public final void delete(I id, DeletePolicy policy) {
        var resource = resources.remove(id);

        if (resource != null)
            applyDeletePolicyToResource(resource, policy);
    }

    @Override
    public final void deleteAll() {
        deleteAll(configuration.getDeletePolicy());
    }

    @Override
    public void deleteAll(DeletePolicy policy) {
        resources.keySet().forEach(id -> delete(id, policy));
    }

    @Override
    public final R get(I id) {
        R resource = resources.get(id);
        if (resource == null)
            throw new ResourceNotFoundException(resourceClass, id);
        return resource;
    }

    @Override
    @PreDestroy
    public void destroy() {
        deleteAll();
    }

    private void applyRewritePolicy(R resource) {
        if (configuration.isRewriteOnSave()) {
            applyDeletePolicyToResource(resource, configuration.getDeletePolicy());
            log.warn("Ресурс {} перезаписан, применена политика удаления по умолчанию", resource.infoName());
        } else
            throw new ResourceAlreadyExistException(resource);
    }

    private void applyDeletePolicyToResource(R resource, DeletePolicy policy) {
        var actionMsg = "удален";
        if (policy == REMOVE_AND_DESTROY) {
            resource.destroy();
            actionMsg += " и освобождён";
        }

        log.debug("Ресурс {} {}", resource.infoName(), actionMsg);
    }
}
