package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.service.impl.dto.CarModelDto;

import static org.vitaly.controller.abstraction.validation.Validator.*;

/**
 * Created by vitaly on 2017-05-11.
 */
public class AddModelValidator implements Validator<CarModelDto> {

    @Override
    public ValidationResult validate(CarModelDto carModelDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        String name = carModelDto.getName();
        stringMatches(name, MODEL_PATTERN, validationResult, ERR_BAD_MODEL_NAME);
        stringWithLengthBetween(name, MIN_LENGTH, MAX_LENGTH, validationResult, ERR_BAD_MODEL_NAME);

        inRange(carModelDto.getDoorCount(), MODEL_MIN_DOORS, MODEL_MAX_DOORS, validationResult, ERR_BAD_DOORS);
        inRange(carModelDto.getSeatCount(), MODEL_MIN_SEATS, MODEL_MAX_SEATS, validationResult, ERR_BAD_SEATS);
        inRange(carModelDto.getSeatCount(), MODEL_MIN_POWER, MODEL_MAX_POWER, validationResult, ERR_BAD_HORSE);

        return validationResult;
    }
}
