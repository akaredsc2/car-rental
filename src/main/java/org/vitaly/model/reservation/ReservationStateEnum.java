package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public enum ReservationStateEnum {
    NEW(new NewState()),
    REJECTED(new RejectedState()),
    APPROVED(new ApprovedState()),
    CANCELED(new CanceledState()),
    PICKED(new PickedUpState()),
    DROPPED(new DroppedOffState())
    ;

    private final ReservationState state;

    ReservationStateEnum(ReservationState state) {
        this.state = state;
    }

    public ReservationState getState() {
        return state;
    }
}
