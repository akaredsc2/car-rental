package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;

import java.util.List;
import java.util.Optional;

/**
 * Car model service
 */
public interface CarModelService {

    /**
     * Add new car model to system
     *
     * @param carModelDto car model dto
     * @return true if added, false otherwise
     */
    boolean addCarModel(CarModelDto carModelDto);

    /**
     * Get all car models
     *
     * @return list of all car model dtos
     */
    List<CarModelDto> getAll();

    /**
     * Update car model
     *
     * @param carModelDto car model dto
     * @return true if update, false otherwise
     */
    boolean updateCarModel(CarModelDto carModelDto);

    /**
     * Find model of car
     *
     * @param carDto car dto
     * @return model of car or empty optional if not found
     */
    Optional<CarModelDto> findModelOfCar(CarDto carDto);
}
