package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.controller.impl.validation.LocaleValidator;
import org.vitaly.controller.impl.validation.RegistrationValidator;
import org.vitaly.controller.impl.validation.SignInValidator;
import org.vitaly.service.impl.dto.UserDto;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vitaly on 2017-05-09.
 */
public class ValidatorFactory {
    private static ValidatorFactory instance = new ValidatorFactory();

    private Validator<UserDto> signInValidator = new SignInValidator();
    private Validator<HttpServletRequest> registrationValidator = new RegistrationValidator();
    private Validator<String> localeValidator = new LocaleValidator();

    private ValidatorFactory() {
    }

    public static ValidatorFactory getInstance() {
        return instance;
    }

    public Validator<UserDto> getSignInValidator() {
        return signInValidator;
    }

    public Validator<HttpServletRequest> getRegistrationValidator() {
        return registrationValidator;
    }

    public Validator<String> getLocaleValidator() {
        return localeValidator;
    }
}
