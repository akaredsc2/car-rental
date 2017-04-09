package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class NewState extends ReservationState {
    @Override
    boolean approve(Reservation reservation) {
        reservation.setState(ReservationStateEnum.APPROVED.getState());
        return true;
    }

    @Override
    boolean reject(Reservation reservation) {
        reservation.setState(ReservationStateEnum.REJECTED.getState());
        return true;
    }

    @Override
    boolean canApprove() {
        return true;
    }

    @Override
    boolean canReject() {
        return true;
    }

    @Override
    public String toString() {
        return "new";
    }
}
