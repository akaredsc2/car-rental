package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class PickedUpState extends ReservationState {
    PickedUpState() {
    }

    @Override
    boolean dropOff(Reservation reservation) {
        reservation.setState(ReservationStateEnum.DROPPED.getState());
        return true;
    }

    @Override
    boolean canDropOff() {
        return true;
    }

    @Override
    public String toString() {
        return "picked";
    }
}
