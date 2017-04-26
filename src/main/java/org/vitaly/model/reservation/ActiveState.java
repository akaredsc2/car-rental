package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class ActiveState extends ReservationState {
    ActiveState() {
    }

    @Override
    boolean close(Reservation reservation) {
        reservation.setState(ReservationStateEnum.CLOSED.getState());
        return true;
    }

    @Override
    boolean canClose() {
        return true;
    }

    @Override
    public String toString() {
        return "active";
    }
}
