package amaralus.apps.hackandslash.graphics.font;

import amaralus.apps.hackandslash.resources.repository.InMemoryResourceRepository;
import amaralus.apps.hackandslash.resources.repository.RepositoryConfiguration;
import org.springframework.stereotype.Service;


import static amaralus.apps.hackandslash.resources.repository.DeletePolicy.REMOVE_AND_DESTROY;

@Service
public class FontRepository extends InMemoryResourceRepository<Font, String> {

    public FontRepository() {
        super(Font.class);
    }

    @Override
    public void configure(RepositoryConfiguration config) {
        config.setDeletePolicy(REMOVE_AND_DESTROY);
    }
}
