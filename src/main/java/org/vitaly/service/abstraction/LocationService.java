package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.List;
import java.util.Optional;

/**
 * Location service
 */
public interface LocationService {

    /**
     * Add new location
     *
     * @param locationDto location dto
     * @return true if added, false otherwise
     */
    boolean addNewLocation(LocationDto locationDto);

    /**
     * Find location of car
     *
     * @param carDto car dto
     * @return location if found, false otherwise
     */
    Optional<LocationDto> findLocationOfCar(CarDto carDto);

    /**
     * Get all locations
     *
     * @return list of locations dtos
     */
    List<LocationDto> getAll();

    /**
     * Update locations
     *
     * @param locationDto location dto
     * @return true if updated false, otherwise
     */
    boolean updateLocation(LocationDto locationDto);
}
