package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.controller.impl.validation.ValidationResultImpl;
import org.vitaly.service.impl.dto.BillDto;

/**
 * Created by vitaly on 2017-05-11.
 */
public class AddDamageBillValidator implements Validator<BillDto> {

    @Override
    public ValidationResult validate(BillDto billDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        Validator.inRange(billDto.getCashAmount(), DAMAGE_MIN, DAMAGE_MAX, validationResult, ERR_DAMAGE);

        return validationResult;
    }
}
