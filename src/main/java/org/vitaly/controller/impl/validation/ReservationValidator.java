package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;

/**
 * Created by vitaly on 2017-05-09.
 */
public class ReservationValidator implements Validator<HttpServletRequest> {

    @Override
    public ValidationResult validate(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResultImpl();

        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        String carId = request.getParameter(properties.getProperty(PARAM_RESERVATION_CAR));
        Validator.isInteger(carId, validationResult, "Car id is not integer number!");

        String pickUpDateTime = request.getParameter(properties.getProperty(PARAM_RESERVATION_PICK));
        String dropOffDateTime = request.getParameter(properties.getProperty(PARAM_RESERVATION_DROP));

        validateLocalDateTimes(pickUpDateTime, dropOffDateTime, validationResult);

        return validationResult;
    }

    private void validateLocalDateTimes(String pickUpDateTime, String dropOffDateTime,
                                        ValidationResult validationResult) {
        if (pickUpDateTime == null || dropOffDateTime == null) {
            validationResult.addErrorMessage(DATE_TIMES_ARE_MESSED_UP);
        } else {
            validateNonNullLocalDateTimes(pickUpDateTime, dropOffDateTime, validationResult);
        }
    }

    private void validateNonNullLocalDateTimes(String pickUpDateTime, String dropOffDateTime,
                                               ValidationResult validationResult) {
        try {
            LocalDateTime pickUp = LocalDateTime.parse(pickUpDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime dropOff = LocalDateTime.parse(dropOffDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();

            if (now.until(pickUp, ChronoUnit.MINUTES) < MINUTES_BETWEEN_RESERVATION_AND_PICK_UP) {
                validationResult.addErrorMessage(TOO_FAST);
            }
            if (now.until(pickUp, ChronoUnit.DAYS) > DAYS_BETWEEN_RESERVATION_AND_PICK_UP) {
                validationResult.addErrorMessage(TOO_LONG);
            }
            if (pickUp.until(dropOff, ChronoUnit.DAYS) < 1) {
                validationResult.addErrorMessage("Drop off must be at least one day after pick up!");
            }
            if (pickUp.until(dropOff, ChronoUnit.MONTHS) > 1) {
                validationResult.addErrorMessage("Car cannot be reserved for more than a month!");
            }
        } catch (DateTimeParseException e) {
            validationResult.addErrorMessage(DATE_TIMES_ARE_MESSED_UP);
        }
    }
}
