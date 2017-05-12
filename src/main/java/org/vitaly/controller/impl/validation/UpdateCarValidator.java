package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.service.impl.dto.CarDto;

import static org.vitaly.controller.abstraction.validation.Validator.*;

/**
 * Created by vitaly on 2017-05-11.
 */
public class UpdateCarValidator implements Validator<CarDto> {
    @Override
    public ValidationResult validate(CarDto carDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        inRange(carDto.getPricePerDay(), CAR_MIN_PRICE, CAR_MAX_PRICE, validationResult, ERR_BAD_PRICE);
        String color = carDto.getColor();
        stringMatches(color, CAR_COLOR_PATTERN, validationResult, ERR_BAD_COLOR);
        stringWithLengthBetween(color, 3, MAX_LENGTH, validationResult, ERR_BAD_COLOR);

        return validationResult;
    }
}
