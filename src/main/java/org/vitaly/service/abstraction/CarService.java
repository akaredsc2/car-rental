package org.vitaly.service.abstraction;

import org.vitaly.model.car.Car;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface CarService {
    boolean addNewCar(CarDto carDto);

    List<Car> findCarsAtLocation(LocationDto locationDto);

    List<Car> findCarsByModel(String model);

    List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to);

    List<Car> getAllMatchingCars(Predicate<Car> carPredicate);

    void updateCar(CarDto carDto);

    void moveCarToLocation(CarDto carDto, LocationDto locationDto);
}
