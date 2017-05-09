package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.controller.impl.validation.*;
import org.vitaly.service.impl.dto.UserDto;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vitaly on 2017-05-09.
 */
public class ValidatorFactory {
    private static ValidatorFactory instance = new ValidatorFactory();

    private SignInValidator signInValidator = new SignInValidator();
    private RegistrationValidator registrationValidator = new RegistrationValidator();
    private Validator<String> localeValidator = new LocaleValidator();
    private Validator<HttpServletRequest> changePasswordValidator = new ChangePasswordValidator();
    private Validator<HttpServletRequest> reservationValidator = new ReservationValidator();

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

    public Validator<HttpServletRequest> getChangePasswordValidator() {
        return changePasswordValidator;
    }

    public Validator<HttpServletRequest> getReservationValidator() {
        return reservationValidator;
    }
}
