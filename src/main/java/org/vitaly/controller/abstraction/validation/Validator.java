package org.vitaly.controller.abstraction.validation;

import java.math.BigDecimal;

/**
 * Created by vitaly on 2017-05-09.
 */
public interface Validator<T> {
    int MIN_AGE = 21;
    int MAX_AGE = 70;

    int MINUTES_BETWEEN_RESERVATION_AND_PICK_UP = 1;
    int DAYS_BETWEEN_RESERVATION_AND_PICK_UP = 7;

    int MODEL_MIN_DOORS = 1;
    int MODEL_MAX_DOORS = 10;

    int MODEL_MIN_SEATS = 1;
    int MODEL_MAX_SEATS = 100;

    int MODEL_MIN_POWER = 1;
    int MODEL_MAX_POWER = 3000;

    BigDecimal CAR_MIN_PRICE = BigDecimal.valueOf(0.01);
    BigDecimal CAR_MAX_PRICE = BigDecimal.valueOf(1_000);

    BigDecimal DAMAGE_MIN = BigDecimal.valueOf(0.01);
    BigDecimal DAMAGE_MAX = BigDecimal.valueOf(100_000);

    int MIN_LENGTH = 4;
    int MAX_LENGTH = 30;

    String LOGIN_PATTERN = "[а-яА-ЯіІїЇєЄёЁ\\w]{4,30}";
    String PASSWORD_PATTERN = "\\w{4,30}";
    String NAME_PATTERN = "([A-ZА-ЯІЇЄ][a-zа-яіїє]+)(\\s([A-ZА-ЯІЇЄ][a-zа-яіїє]+))+";
    String UKR_PASSPORT_PATTERN = "([А-Я]){2}\\d{6}";
    String RUS_PASSPORT_PATTERN = "\\d{10}";
    String PASSPORT_PATTERN = UKR_PASSPORT_PATTERN + "|" + RUS_PASSPORT_PATTERN;
    String URK_DRIVER_LICENCE = "[А-Я]{3}\\d{6}";
    String RUS_DRIVER_LICENCE = "\\d{2}([А-Я]){2}\\d{6}";
    String DRIVER_LICENCE_PATTERN = URK_DRIVER_LICENCE + "|" + RUS_DRIVER_LICENCE;
    String LOCATION_PATTERN = "[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+(\\s+[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+)*";
    String MODEL_PATTERN = "[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+(\\s+[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+)*";
    String CAR_PLATE_PATTERN = "[А-Я]{2}\\d{4}[А-Я]{2}";
    String CAR_COLOR_PATTERN = "[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+(\\s+[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+)*";
    String REJECTION_PATTERN = "[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+(\\s+[a-zA-Zа-яА-ЯіІїЇєЄ\\d]+)*";

    String ERR_BAD_LOGIN = "err.bad.login";
    String ERR_BAD_PASSWORD = "err.bad.password";
    String ERR_PASS_NOT = "err.pass.not";
    String ERR_BAD_NAME = "err.bad.name";
    String ERR_BAD_PASSPORT = "err.bad.passport";
    String ERR_BAD_DRIVER = "err.bad.driver";
    String ERR_BAD_DATE = "err.bad.date";
    String ERR_YOUNG = "err.young";
    String ERR_OLD = "err.old";
    String ERR_FAST = "err.fast";
    String ERR_LONG = "err.long";
    String ERR_DROP_SOON = "err.drop.soon";
    String ERR_RES_TOO_LONG = "err.res.too.long";
    String ERR_BAD_LOCATION_STATE = "err.location.state";
    String ERR_BAD_LOCATION_CITY = "err.location.city";
    String ERR_BAD_LOCATION_STREET = "err.location.street";
    String ERR_BAD_LOCATION_BUILDING = "err.location.building";
    String ERR_BAD_MODEL_NAME = "err.model.name";
    String ERR_BAD_DOORS = "err.model.doors";
    String ERR_BAD_SEATS = "err.model.seats";
    String ERR_BAD_HORSE = "err.model.horse";
    String ERR_BAD_PLATE = "err.bad.plate";
    String ERR_BAD_PRICE = "err.bad.price";
    String ERR_BAD_COLOR = "err.bad.color";
    String ERR_REJECTION = "err.rejection";
    String ERR_DAMAGE = "err.damage";

    String ERR_ADD_BILL = "err.add.bill";
    String ERR_WRONG_COMMAND = "err.wrong.command";
    String ERR_CREATE_RES = "err.create.res";
    String ERR_CHANGE_CAR_STATE = "err.change.car.state";
    String ERR_CHANGE_PASSWORD = "err.change.password";
    String ERR_ASSIGN_ADMIN = "err.assign.admin";
    String ERR_CONFIRM_BILL = "err.confirm.bill";
    String ERR_ADD_CAR_MODEL = "err.add.model";
    String ERR_PAY = "err.bill.pay";
    String ERR_ADD_CAR = "err.add.car";
    String ERR_MOVE_CAR = "err.move.car";
    String ERR_UPD_CAR = "err.upd.car";
    String ERR_ADD_LOC = "err.add.loc";
    String ERR_CANCEL_RES = "err.cancel.res";
    String ERR_CHANGE_RES = "err.change.res";
    String ERR_REG = "err.reg";
    String ERR_SIGN_IN = "err.sign.in";
    String ERR_UPDATE_PHOTO = "err.update.photo";
    String ERR_CHANGE_ROLE = "err.change.role";

    ValidationResult validate(T t);

    static void stringMatches(String string, String pattern, ValidationResult validationResult, String errorMessage) {
        if (string == null || !string.matches(pattern)) {
            validationResult.addErrorMessage(errorMessage);
        }
    }

    static void stringWithLengthBetween(String string, int min, int max,
                                        ValidationResult validationResult, String errorMessage) {
        if (string != null) {
            inRange(string.length(), min, max, validationResult, errorMessage);
        } else {
            validationResult.addErrorMessage(errorMessage);
        }
    }

    static void inRange(int value, int min, int max, ValidationResult validationResult, String errorMessage) {
        if (value < min || value > max) {
            validationResult.addErrorMessage(errorMessage);
        }
    }

    static void inRange(BigDecimal value, BigDecimal min, BigDecimal max,
                        ValidationResult validationResult, String errorMessage) {
        if (value == null || value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            validationResult.addErrorMessage(errorMessage);
        }
    }
}
