package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class ApprovedState extends ReservationState {
    @Override
    boolean cancel(Reservation reservation) {
        reservation.setState(ReservationStateEnum.CANCELED.getState());
        return true;
    }

    @Override
    boolean pickUp(Reservation reservation) {
        reservation.setState(ReservationStateEnum.PICKED.getState());
        return true;
    }

    @Override
    boolean canCancel() {
        return true;
    }

    @Override
    boolean canPickUp() {
        return true;
    }

    @Override
    public String toString() {
        return "approved";
    }
}
