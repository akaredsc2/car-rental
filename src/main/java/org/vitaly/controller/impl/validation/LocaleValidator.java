package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.util.SupportedLocaleEnum;

/**
 * Created by vitaly on 2017-05-09.
 */
public class LocaleValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String locale) {
        ValidationResult validationResult = new ValidationResultImpl();

        if (locale == null || !SupportedLocaleEnum.of(locale).isPresent()) {
            validationResult.addErrorMessage("Unsupported locale!");
        }

        return validationResult;
    }
}
