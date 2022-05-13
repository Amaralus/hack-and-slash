package amaralus.apps.hackandslash.resources.repository;

import amaralus.apps.hackandslash.resources.Resource;

public interface ResourceRepository<R extends Resource<I>, I extends Comparable<I>> {

    R save(R resource);

    void delete(I id);

    void delete(I id, DeletePolicy policy);

    void deleteAll();

    void deleteAll(DeletePolicy policy);

    R get(I id);
}
