package org.vitaly.model.reservation;

/**
 * New reservation state
 */
public class NewState extends ReservationState {
    NewState() {
    }

    /**
     * Approve reservation
     *
     * @param reservation context reservation
     * @return true
     */
    @Override
    boolean approve(Reservation reservation) {
        reservation.setState(ReservationStateEnum.APPROVED.getState());
        return true;
    }

    /**
     * Reject reservation
     *
     * @param reservation context reservation
     * @return true
     */
    @Override
    boolean reject(Reservation reservation) {
        reservation.setState(ReservationStateEnum.REJECTED.getState());
        return true;
    }

    @Override
    public String toString() {
        return "new";
    }
}
