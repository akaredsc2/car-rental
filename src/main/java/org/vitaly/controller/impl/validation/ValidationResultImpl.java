package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitaly on 2017-05-09.
 */
public class ValidationResultImpl implements ValidationResult {
    private List<String> errorMessages = new ArrayList<>();

    @Override
    public boolean isValid() {
        return errorMessages.isEmpty();
    }

    @Override
    public void addErrorMessage(String message) {
        errorMessages.add(message);
    }

    @Override
    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
