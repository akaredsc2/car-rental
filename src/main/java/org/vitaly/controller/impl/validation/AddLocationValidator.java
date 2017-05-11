package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.service.impl.dto.LocationDto;

/**
 * Created by vitaly on 2017-05-11.
 */
public class AddLocationValidator implements Validator<LocationDto> {

    @Override
    public ValidationResult validate(LocationDto locationDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        Validator.stringMatches(locationDto.getState(), LOCATION_PATTERN, validationResult, ERR_BAD_LOCATION_STATE);
        Validator.stringMatches(locationDto.getCity(), LOCATION_PATTERN, validationResult, ERR_BAD_LOCATION_CITY);
        Validator.stringMatches(locationDto.getStreet(), LOCATION_PATTERN, validationResult, ERR_BAD_LOCATION_STREET);
        Validator.stringMatches(locationDto.getBuilding(), LOCATION_PATTERN,
                validationResult, ERR_BAD_LOCATION_BUILDING);

        return validationResult;
    }
}
