package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.controller.impl.command.reservation.ChangeReservationStateCommand;
import org.vitaly.controller.impl.validation.*;
import org.vitaly.service.impl.dto.*;

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
    private Validator<LocationDto> addLocationValidator = new AddLocationValidator();
    private Validator<CarModelDto> addModelValidator = new AddModelValidator();
    private Validator<CarDto> addCarValidator = new AddCarValidator();
    private Validator<ReservationDto> changeReservationStateValidator = new ChangeReservationStateValidator();
    private Validator<CarDto> updateCarValidator = new UpdateCarValidator();
    private Validator<BillDto> addDamageBillValidator = new AddDamageBillValidator();

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

    public Validator<LocationDto> getAddLocationValidator() {
        return addLocationValidator;
    }

    public Validator<CarModelDto> getAddModelValidator() {
        return addModelValidator;
    }

    public Validator<CarDto> getAddCarValidator() {
        return addCarValidator;
    }

    public Validator<ReservationDto> getChangeReservationStateValidator() {
        return changeReservationStateValidator;
    }

    public Validator<CarDto> getUpdateCarValidator() {
        return updateCarValidator;
    }

    public Validator<BillDto> getAddDamageBillValidator() {
        return addDamageBillValidator;
    }
}
