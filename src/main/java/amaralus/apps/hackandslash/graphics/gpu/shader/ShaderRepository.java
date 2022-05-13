package amaralus.apps.hackandslash.graphics.gpu.shader;

import amaralus.apps.hackandslash.resources.repository.InMemoryResourceRepository;
import amaralus.apps.hackandslash.resources.repository.RepositoryConfiguration;
import org.springframework.stereotype.Service;


import static amaralus.apps.hackandslash.resources.repository.DeletePolicy.REMOVE_AND_DESTROY;

@Service
public class ShaderRepository extends InMemoryResourceRepository<Shader, String> {

    public ShaderRepository() {
        super(Shader.class);
    }

    @Override
    public void configure(RepositoryConfiguration config) {
        config.setDeletePolicy(REMOVE_AND_DESTROY);
    }
}
