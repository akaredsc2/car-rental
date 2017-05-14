package org.vitaly.model;

/**
 * Classes implementing this interface are entities in created
 * system. Each entity has its own id.
 * @author vitaly
 */
public interface Entity {

    /**
     * Returns id of an entity
     * @return id of an entity
     */
    long getId();
}
