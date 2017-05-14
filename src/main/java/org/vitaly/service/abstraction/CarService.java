package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface CarService {
    /**
     * Add new car to system
     *
     * @param carDto car dto
     * @return true if added, false otherwise
     */
    boolean addNewCar(CarDto carDto);

    /**
     * Find car by id
     *
     * @param carId car id
     * @return car or empty optional if not found
     */
    Optional<CarDto> findCarById(long carId);

    /**
     * Find all cars at location
     *
     * @param locationDto location dto
     * @return list of car dtos at location
     */
    List<CarDto> findCarsAtLocation(LocationDto locationDto);

    /**
     * Find cars by model
     *
     * @param carModelDto model dto
     * @return list of car model dtos
     */
    List<CarDto> findCarsByModel(CarModelDto carModelDto);

    /**
     * Gets all cars
     *
     * @return list of all car dtos
     */
    List<CarDto> getAllCars();

    /**
     * Update car
     *
     * @param carDto car dto
     * @return true if update, false otherwise
     */
    boolean updateCar(CarDto carDto);

    /**
     * Move car to location
     *
     * @param carDto      car dto
     * @param locationDto location dto
     * @return true if moved, false otherwise
     */
    boolean moveCarToLocation(CarDto carDto, LocationDto locationDto);

    /**
     * Change car state
     *
     * @param carDto   car dto
     * @param carState car state
     * @return true if changed, false otherwise
     */
    boolean changeCarState(CarDto carDto, String carState);

    /**
     * Find car for reservation
     *
     * @param reservationDto reservation dto
     * @return car if found, empty optional otherwise
     */
    Optional<CarDto> findCarForReservation(ReservationDto reservationDto);
}
