package amaralus.apps.hackandslash.graphics.gpu.texture.repository;

import amaralus.apps.hackandslash.graphics.gpu.texture.Texture;
import amaralus.apps.hackandslash.resources.repository.InMemoryResourceRepository;
import amaralus.apps.hackandslash.resources.repository.RepositoryConfiguration;
import org.springframework.stereotype.Service;

import static amaralus.apps.hackandslash.resources.repository.DeletePolicy.REMOVE_AND_DESTROY;

@Service
public class TextureRepository extends InMemoryResourceRepository<Texture, String> {

    public TextureRepository() {
        super(Texture.class);
    }

    @Override
    public void configure(RepositoryConfiguration config) {
        config.setDeletePolicy(REMOVE_AND_DESTROY);
    }
}
