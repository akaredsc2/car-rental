package org.vitaly.dao.abstraction;

import org.vitaly.model.Entity;

import java.util.List;
import java.util.Optional;

/**
 * Abstract dao for entities of system
 */
public interface AbstractDao<E extends Entity> {

    /**
     * Find entity by id
     *
     * @param id id of entity
     * @return entity or empty optional if entity with supplied id not found
     */
    Optional<E> findById(long id);

    /**
     * Gets all entities of this type
     *
     * @return list of all entities
     */
    List<E> getAll();

    /**
     * Creates entity of type
     *
     * @param entity entity
     * @return id of created entity or empty optional if creation failed
     */
    Optional<Long> create(E entity);

    /**
     * Updated entity with id with supplied entity
     *
     * @param id     id of entity to update
     * @param entity parameters to update
     * @return count of affected entities
     */
    int update(long id, E entity);
}
