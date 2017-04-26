package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public enum ReservationStateEnum {
    NEW(new NewState()),
    REJECTED(new RejectedState()),
    APPROVED(new ApprovedState()),
    CANCELED(new CanceledState()),
    ACTIVE(new ActiveState()),
    CLOSED(new ClosedState());

    private final ReservationState state;

    ReservationStateEnum(ReservationState state) {
        this.state = state;
    }

    public ReservationState getState() {
        return state;
    }
}
