package org.vitaly.dao.abstraction;

import org.vitaly.model.car.Car;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by vitaly on 2017-03-27.
 */
public interface CarDao extends AbstractDao<Car> {
    boolean moveCarToLocation(long carId, long locationId);

    List<Car> findCarsAtLocation(long locationId);

    List<Car> findCarsByModel(String model);

    List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to);

    List<String> findAllCarModels();
}
