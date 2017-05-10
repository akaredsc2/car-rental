package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 2017-05-09.
 */
public class ChangePasswordValidator implements Validator<HttpServletRequest> {

    @Override
    public ValidationResult validate(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResultImpl();

        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        String oldPassword = request.getParameter(properties.getProperty(PARAM_PASS_OLD));
        String newPassword = request.getParameter(properties.getProperty(PARAM_PASS_NEW));
        String repeatPassword = request.getParameter(properties.getProperty(PARAM_PASS_REPEAT));

        Validator.stringMatches(oldPassword, PASSWORD_PATTERN, validationResult, "Old password does not match regex");
        Validator.stringMatches(newPassword, PASSWORD_PATTERN, validationResult, "New password does not match regex");
        Validator.stringMatches(repeatPassword, PASSWORD_PATTERN,
                validationResult, "Repeat password does not match regex");
        String currentPassword = ((UserDto) request.getSession().getAttribute(SESSION_USER)).getPassword();
        if (!oldPassword.equals(currentPassword)) {
            validationResult.addErrorMessage("Wrong old password");
        }
        if (!newPassword.equals(repeatPassword)) {
            validationResult.addErrorMessage(ERR_PASS_NOT);
        }
        return validationResult;
    }
}
