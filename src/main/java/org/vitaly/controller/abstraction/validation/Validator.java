package org.vitaly.controller.abstraction.validation;

/**
 * Created by vitaly on 2017-05-09.
 */
public interface Validator<T> {
    String LOGIN_PATTERN = "[а-яА-ЯіІїЇєЄёЁ\\w]{4,45}";
    String PASSWORD_PATTERN = "\\w{4,10}";
    String NAME_PATTERN = "([A-ZА-ЯІЇЄ][a-zа-яіїє]+)(\\s([A-ZА-ЯІЇЄ][a-zа-яіїє]+))+";
    String UKR_PASSPORT_PATTERN = "([А-Я]){2}\\d{6}";
    String RUS_PASSPORT_PATTERN = "\\d{10}";
    String PASSPORT_PATTERN = UKR_PASSPORT_PATTERN + "|" + RUS_PASSPORT_PATTERN;
    String URK_DRIVER_LICENCE = "([А-Я]){3}\\d{6}";
    String RUS_DRIVER_LICENCE = "\\d{2}([А-Я]){2}\\d{6}";
    String DRIVER_LICENCE_PATTERN = URK_DRIVER_LICENCE + "|" + RUS_DRIVER_LICENCE;

    String LOGIN_DOES_NOT_MATCH_REGEX = "Login does not match regex";
    String PASSWORD_DOES_NOT_MATCH_REGEX = "Password does not match regex";
    String PASSWORDS_ARE_NOT_EQUAL = "Passwords are not equal!";
    String NAME_DOES_NOT_MATCH_PATTERN = "Name does not match pattern!";
    String PASSPORT_DOES_NOT_MATCH_PATTERN = "Passport number does not match pattern!";
    String DRIVER_LICENCE_DOES_NOT_MATCH_PATTERN = "Driver licence number does not match pattern!";
    String BIRTH_DATE_DOES_NOT_MATCH_PATTERN = "Birth date does not match pattern!";
    String TOO_YOUNG = "You must be at least 21 years old to be able to sign up";
    String TOO_OLD = "You must be not older than 70 years old to be able to sign up";

    ValidationResult validate(T t);

    static void stringMatches(String string, String pattern, ValidationResult validationResult, String errorMessage) {
        if (string == null || !string.matches(pattern)) {
            validationResult.addErrorMessage(errorMessage);
        }
    }
}
