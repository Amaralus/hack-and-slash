package amaralus.apps.hackandslash.graphics.sprites.repository;

import amaralus.apps.hackandslash.graphics.sprites.Sprite;
import amaralus.apps.hackandslash.resources.repository.InMemoryResourceRepository;
import org.springframework.stereotype.Service;

@Service
public class SpriteRepository extends InMemoryResourceRepository<Sprite, String> {

    public SpriteRepository() {
        super(Sprite.class);
    }

}
