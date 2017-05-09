package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface CarService {
    boolean addNewCar(CarDto carDto);

    List<CarDto> findCarsAtLocation(LocationDto locationDto);

    List<CarDto> findCarsByModel(CarModelDto carModelDto);

    List<CarDto> findCarsWithPriceBetween(BigDecimal from, BigDecimal to);

    List<CarDto> getAllCars();

    boolean updateCar(CarDto carDto);

    boolean moveCarToLocation(CarDto carDto, LocationDto locationDto);

    boolean changeCarState(CarDto carDto, String carState);

    Optional<CarDto> findCarForReservation(ReservationDto reservationDto);
}
