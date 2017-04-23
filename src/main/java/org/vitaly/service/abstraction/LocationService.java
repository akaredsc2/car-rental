package org.vitaly.service.abstraction;

import org.vitaly.model.location.Location;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface LocationService {
    boolean addNewLocation(LocationDto locationDto);

    Optional<LocationDto> findLocationOfCar(CarDto carDto);

    List<LocationDto> getAllMatchingLocations(Predicate<Location> locationPredicate);

    boolean changeLocationPhotoUrl(LocationDto locationDto, String photoUrl);
}
