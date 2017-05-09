package org.vitaly.controller.abstraction.validation;

import java.util.List;

/**
 * Created by vitaly on 2017-05-09.
 */
public interface ValidationResult {
    boolean isValid();

    void addErrorMessage(String message);

    List<String> getErrorMessages();
}
