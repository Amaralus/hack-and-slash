package amaralus.apps.hackandslash.resources.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RepositoryConfiguration {

    private DeletePolicy deletePolicy = DeletePolicy.REMOVE;
    private boolean rewriteOnSave = false;
}
