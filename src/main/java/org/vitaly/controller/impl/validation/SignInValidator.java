package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.service.impl.dto.UserDto;

import static org.vitaly.controller.abstraction.validation.Validator.stringMatches;

/**
 * Created by vitaly on 2017-05-09.
 */
public class SignInValidator implements Validator<UserDto> {

    @Override
    public ValidationResult validate(UserDto userDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        stringMatches(userDto.getLogin(), LOGIN_PATTERN, validationResult, ERR_BAD_LOGIN);
        stringMatches(userDto.getPassword(), PASSWORD_PATTERN, validationResult, ERR_BAD_PASSWORD);

        return validationResult;
    }
}
