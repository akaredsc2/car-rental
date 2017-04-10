package org.vitaly.service.abstraction;

import org.vitaly.model.car.Car;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface CarService {
    Optional<Car> findById(long id);

    Optional<Long> findIdOfEntity(Car car);

    List<Car> getAll();

    Optional<Long> create(Car car);

    int update(long id, Car car);

    boolean addCarToLocation(long carId, long locationId);

    List<Car> findCarsAtLocation(long locationId);

    List<Car> findCarsByModel(String model);

    List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to);

    List<String> findAllCarModels();
}
