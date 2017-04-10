package org.vitaly.service.abstraction;

import org.vitaly.model.location.Location;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface LocationService {
    Optional<Location> findById(long id);

    Optional<Long> findIdOfEntity(Location entity);

    List<Location> getAll();

    Optional<Long> create(Location entity);

    Optional<Location> findLocationByCarId(long carId);
}
