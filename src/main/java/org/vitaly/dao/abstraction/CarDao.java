package org.vitaly.dao.abstraction;

import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;

import java.util.List;
import java.util.Optional;

/**
 * Dao for cars
 */
public interface CarDao extends AbstractDao<Car> {

    /**
     * Moves car to location
     *
     * @param carId      car id
     * @param locationId location id
     * @return true if car moved, false otherwise
     */
    boolean moveCarToLocation(long carId, long locationId);

    /**
     * Finds cars at location
     *
     * @param locationId location id
     * @return list of cars at location
     */
    List<Car> findCarsAtLocation(long locationId);

    /**
     * Finds cars with model
     *
     * @param carModelId location model
     * @return list of cars with model
     */
    List<Car> findCarsByModel(long carModelId);

    /**
     * Changes car state
     *
     * @param carId car id
     * @param state car state
     * @return true if car state changed, false otherwise
     */
    boolean changeCarState(long carId, CarState state);

    /**
     * Find car in reservation
     *
     * @param reservationId reservation id
     * @return car or empty optional if car not present
     */
    Optional<Car> findCarByReservation(long reservationId);
}
