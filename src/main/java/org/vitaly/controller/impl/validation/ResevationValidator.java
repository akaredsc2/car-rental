package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_RESERVATION_CAR;
import static org.vitaly.util.constants.RequestParameters.PARAM_RESERVATION_PICK;

/**
 * Created by vitaly on 2017-05-09.
 */
public class ResevationValidator implements Validator<HttpServletRequest> {

    @Override
    public ValidationResult validate(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResultImpl();
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        String carId = request.getParameter(properties.getProperty(PARAM_RESERVATION_CAR));
        Validator.stringMatches(carId, "[-+]{0,1}\\d+", validationResult, "Car id is not a number!");

        String pickUpDate = request.getParameter(properties.getProperty(PARAM_RESERVATION_PICK));


        return null;
    }
}
