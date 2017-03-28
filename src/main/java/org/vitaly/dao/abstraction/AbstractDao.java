package org.vitaly.dao.abstraction;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Created by vitaly on 2017-03-26.
 */
public interface AbstractDao<E> {
    Optional<E> findById(long id);

    OptionalLong findIdOfEntity(E entity);

    List<E> getAll();

    OptionalLong create(E entity);

    int update(Long id, E entity);
}
