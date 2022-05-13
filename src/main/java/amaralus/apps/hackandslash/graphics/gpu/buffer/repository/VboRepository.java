package amaralus.apps.hackandslash.graphics.gpu.buffer.repository;

import amaralus.apps.hackandslash.graphics.gpu.buffer.VertexBufferObject;
import amaralus.apps.hackandslash.resources.repository.InMemoryResourceRepository;
import amaralus.apps.hackandslash.resources.repository.RepositoryConfiguration;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.resources.repository.DeletePolicy.REMOVE_AND_DESTROY;

@Service
public class VboRepository extends InMemoryResourceRepository<VertexBufferObject<?>, String> {

    public VboRepository() {
        super(VertexBufferObject.class);
    }

    @Override
    public void configure(RepositoryConfiguration config) {
        config.setDeletePolicy(REMOVE_AND_DESTROY);
    }
}
