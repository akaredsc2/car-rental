package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;

/**
 * Created by vitaly on 2017-05-09.
 */
public class RegistrationValidator implements Validator<HttpServletRequest> {

    @Override
    public ValidationResult validate(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResultImpl();

        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        String login = request.getParameter(properties.getProperty(PARAM_USER_LOGIN));
        String password = request.getParameter(properties.getProperty(PARAM_USER_PASSWORD));
        String repeatPassword = request.getParameter(properties.getProperty(PARAM_PASS_REPEAT));
        String fullName = request.getParameter(properties.getProperty(PARAM_USER_NAME));
        String birthDate = request.getParameter(properties.getProperty(PARAM_USER_BIRTHDAY));
        String passport = request.getParameter(properties.getProperty(PARAM_USER_PASSPORT));
        String driverLicence = request.getParameter(properties.getProperty(PARAM_USER_DRIVER));

        Validator.stringMatches(login, LOGIN_PATTERN, validationResult, ERR_BAD_LOGIN);
        Validator.stringMatches(password, PASSWORD_PATTERN, validationResult, ERR_BAD_PASSWORD);
        Validator.stringMatches(repeatPassword, PASSWORD_PATTERN, validationResult, ERR_BAD_PASSWORD);
        if (!password.equals(repeatPassword)) {
            validationResult.addErrorMessage(ERR_PASS_NOT);
        }
        Validator.stringMatches(fullName, NAME_PATTERN, validationResult, ERR_BAD_NAME);

        validateBirthDate(validationResult, birthDate);

        Validator.stringMatches(passport, PASSPORT_PATTERN, validationResult, ERR_BAD_PASSPORT);
        Validator.stringMatches(driverLicence, DRIVER_LICENCE_PATTERN,
                validationResult, ERR_BAD_DRIVER);

        return validationResult;
    }

    private void validateBirthDate(ValidationResult validationResult, String birthDate) {
        if (birthDate == null) {
            validationResult.addErrorMessage(ERR_BAD_DATE);
        } else {
            validateNonNullLocalDate(validationResult, birthDate);
        }
    }

    private void validateNonNullLocalDate(ValidationResult validationResult, String birthDate) {
        try {
            LocalDate localDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate now = LocalDate.now();

            if (localDate.until(now, ChronoUnit.YEARS) < MIN_AGE) {
                validationResult.addErrorMessage(ERR_YOUNG);
            }
            if (localDate.until(now, ChronoUnit.YEARS) > MAX_AGE) {
                validationResult.addErrorMessage(ERR_OLD);
            }
        } catch (DateTimeParseException e) {
            validationResult.addErrorMessage(ERR_BAD_DATE);
        }
    }
}
