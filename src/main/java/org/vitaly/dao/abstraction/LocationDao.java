package org.vitaly.dao.abstraction;

import org.vitaly.model.location.Location;

import java.util.Optional;

/**
 * Dao for locations
 */
public interface LocationDao extends AbstractDao<Location> {

    /**
     * Find location of car
     *
     * @param carId car id
     * @return location of car or empty optional if no location found
     */
    Optional<Location> findLocationByCarId(long carId);
}
