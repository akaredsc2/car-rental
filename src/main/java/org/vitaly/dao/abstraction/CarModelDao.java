package org.vitaly.dao.abstraction;

import org.vitaly.model.carModel.CarModel;

import java.util.Optional;

/**
 * Dao for car model
 */
public interface CarModelDao extends AbstractDao<CarModel> {

    /**
     * Find model of car
     *
     * @param carId car id
     * @return car model or empty optional if car model wa not found
     */
    Optional<CarModel> findModelOfCar(long carId);
}
