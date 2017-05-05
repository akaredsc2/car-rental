package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class ApprovedState extends ReservationState {
    ApprovedState() {
    }

    @Override
    boolean cancel(Reservation reservation) {
        reservation.setState(ReservationStateEnum.CANCELED.getState());
        return true;
    }

    @Override
    boolean activate(Reservation reservation) {
        reservation.setState(ReservationStateEnum.ACTIVE.getState());
        return true;
    }

    @Override
    public String toString() {
        return "approved";
    }
}
