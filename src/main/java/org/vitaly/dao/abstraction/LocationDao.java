package org.vitaly.dao.abstraction;

import org.vitaly.model.location.Location;

import java.util.Optional;

/**
 * Created by vitaly on 2017-03-26.
 */
public interface LocationDao extends AbstractDao<Location> {
    Optional<Location> findLocationByCarId(long carId);
}
