package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.service.impl.dto.CarDto;

/**
 * Created by vitaly on 2017-05-11.
 */
public class UpdateCarValidator implements Validator<CarDto> {
    @Override
    public ValidationResult validate(CarDto carDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        Validator.inRange(carDto.getPricePerDay(), CAR_MIN_PRICE, CAR_MAX_PRICE, validationResult, ERR_BAD_PRICE);
        Validator.stringMatches(carDto.getColor(), CAR_COLOR_PATTERN, validationResult, ERR_BAD_COLOR);

        return validationResult;
    }
}
