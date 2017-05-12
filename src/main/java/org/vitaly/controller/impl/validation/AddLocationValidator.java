package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.service.impl.dto.LocationDto;

import static org.vitaly.controller.abstraction.validation.Validator.stringMatches;
import static org.vitaly.controller.abstraction.validation.Validator.stringWithLengthBetween;

/**
 * Created by vitaly on 2017-05-11.
 */
public class AddLocationValidator implements Validator<LocationDto> {

    @Override
    public ValidationResult validate(LocationDto locationDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        String state = locationDto.getState();
        String city = locationDto.getCity();
        String street = locationDto.getStreet();
        String building = locationDto.getBuilding();

        stringMatches(state, LOCATION_PATTERN, validationResult, ERR_BAD_LOCATION_STATE);
        stringWithLengthBetween(state, MIN_LENGTH, MAX_LENGTH, validationResult, ERR_BAD_LOCATION_STATE);

        stringMatches(city, LOCATION_PATTERN, validationResult, ERR_BAD_LOCATION_CITY);
        stringWithLengthBetween(city, MIN_LENGTH, MAX_LENGTH, validationResult, ERR_BAD_LOCATION_CITY);

        stringMatches(street, LOCATION_PATTERN, validationResult, ERR_BAD_LOCATION_STREET);
        stringWithLengthBetween(street, MIN_LENGTH, MAX_LENGTH, validationResult, ERR_BAD_LOCATION_STREET);

        stringMatches(building, LOCATION_PATTERN, validationResult, ERR_BAD_LOCATION_BUILDING);
        stringWithLengthBetween(building, MIN_LENGTH, MAX_LENGTH, validationResult, ERR_BAD_LOCATION_BUILDING);

        return validationResult;
    }
}
