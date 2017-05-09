package org.vitaly.controller.abstraction.validation;

/**
 * Created by vitaly on 2017-05-09.
 */
public interface Validator<T> {
    int MIN_AGE = 21;
    int MAX_AGE = 70;
    int MINUTES_BETWEEN_RESERVATION_AND_PICK_UP = 1;
    int DAYS_BETWEEN_RESERVATION_AND_PICK_UP = 7;

    String LOGIN_PATTERN = "[а-яА-ЯіІїЇєЄёЁ\\w]{4,45}";
    String PASSWORD_PATTERN = "\\w{4,10}";
    String NAME_PATTERN = "([A-ZА-ЯІЇЄ][a-zа-яіїє]+)(\\s([A-ZА-ЯІЇЄ][a-zа-яіїє]+))+";
    String UKR_PASSPORT_PATTERN = "([А-Я]){2}\\d{6}";
    String RUS_PASSPORT_PATTERN = "\\d{10}";
    String PASSPORT_PATTERN = UKR_PASSPORT_PATTERN + "|" + RUS_PASSPORT_PATTERN;
    String URK_DRIVER_LICENCE = "([А-Я]){3}\\d{6}";
    String RUS_DRIVER_LICENCE = "\\d{2}([А-Я]){2}\\d{6}";
    String DRIVER_LICENCE_PATTERN = URK_DRIVER_LICENCE + "|" + RUS_DRIVER_LICENCE;
    String INTEGER_PATTERN = "[-+]{0,1}\\d+";

    String LOGIN_DOES_NOT_MATCH_REGEX = "Login does not match regex";
    String PASSWORD_DOES_NOT_MATCH_REGEX = "Password does not match regex";
    String PASSWORDS_ARE_NOT_EQUAL = "Passwords are not equal!";
    String NAME_DOES_NOT_MATCH_PATTERN = "Name does not match pattern!";
    String PASSPORT_DOES_NOT_MATCH_PATTERN = "Passport number does not match pattern!";
    String DRIVER_LICENCE_DOES_NOT_MATCH_PATTERN = "Driver licence number does not match pattern!";
    String BIRTH_DATE_DOES_NOT_MATCH_PATTERN = "Birth date does not match pattern!";
    String TOO_YOUNG = "You must be at least 21 years old to be able to sign up";
    String TOO_OLD = "You must be not older than 70 years old to be able to sign up";
    String TOO_FAST = "Reservation pick up date must be at least one minute in the future!";
    String TOO_LONG = "Reservation pick up must be no longer than a week after reservation date";
    String DATE_TIMES_ARE_MESSED_UP = "Date times are messed up!";

    ValidationResult validate(T t);

    static void stringMatches(String string, String pattern, ValidationResult validationResult, String errorMessage) {
        if (string == null || !string.matches(pattern)) {
            validationResult.addErrorMessage(errorMessage);
        }
    }

    static void isInteger(String string, ValidationResult validationResult, String errorMessage) {
        stringMatches(string, INTEGER_PATTERN, validationResult, errorMessage);
    }
}
