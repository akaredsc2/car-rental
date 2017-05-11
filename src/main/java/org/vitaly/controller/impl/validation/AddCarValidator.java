package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.CarDto;

/**
 * Created by vitaly on 2017-05-11.
 */
public class AddCarValidator implements Validator<CarDto> {

    @Override
    public ValidationResult validate(CarDto carDto) {
        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getUpdateCarValidator()
                .validate(carDto);

        Validator.stringMatches(carDto.getRegistrationPlate(), CAR_PLATE_PATTERN, validationResult, ERR_BAD_PLATE);

        return validationResult;
    }
}
