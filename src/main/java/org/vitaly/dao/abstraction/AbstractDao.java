package org.vitaly.dao.abstraction;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Created by vitaly on 2017-03-26.
 */
public interface AbstractDao<E> {
    Optional<E> findById(long id);

    Optional<Long> findIdOfEntity(E entity);

    List<E> getAll();

    Optional<Long> create(E entity);

    int update(long id, E entity);
}
