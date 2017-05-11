package org.vitaly.controller.impl.validation;

import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.service.impl.dto.ReservationDto;

/**
 * Created by vitaly on 2017-05-11.
 */
public class ChangeReservationStateValidator implements Validator<ReservationDto> {

    @Override
    public ValidationResult validate(ReservationDto reservationDto) {
        ValidationResult validationResult = new ValidationResultImpl();

        ReservationState state = reservationDto.getState();
        if (state != null && state == ReservationStateEnum.REJECTED.getState()) {
            Validator.stringMatches(reservationDto.getRejectionReason(), REJECTION_PATTERN,
                    validationResult, ERR_REJECTION);
        }

        return validationResult;
    }
}
