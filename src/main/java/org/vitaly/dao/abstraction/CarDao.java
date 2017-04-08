package org.vitaly.dao.abstraction;

import org.vitaly.model.car.Car;
import org.vitaly.model.location.Location;

import java.util.List;

/**
 * Created by vitaly on 2017-03-27.
 */
public interface CarDao extends AbstractDao<Car> {
    boolean addCarToLocation(long carId, long locationId);

    List<Car> getCarsAtLocation(long locationId);
}
