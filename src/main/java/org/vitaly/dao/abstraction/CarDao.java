package org.vitaly.dao.abstraction;

import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-03-27.
 */
public interface CarDao extends AbstractDao<Car> {
    boolean moveCarToLocation(long carId, long locationId);

    List<Car> findCarsAtLocation(long locationId);

    List<Car> findCarsByModel(long carModelId);

    List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to);

    boolean changeCarState(long carId, CarState state);

    Optional<Car> findCarByReservation(long reservationId);
}
