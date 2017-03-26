package org.vitaly.dao.abstraction;

import org.vitaly.model.location.Location;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-03-26.
 */
public interface AbstractDao<E> {
    Optional<E> findById(Long id);

    Optional<Long> findIdOfEntity(E entity);

    List<E> getAll();

    Optional<Long> create(E entity);

    void update(Long id, Location entity);
}
