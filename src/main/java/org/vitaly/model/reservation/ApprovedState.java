package org.vitaly.model.reservation;

/**
 * Approved reservation state
 */
public class ApprovedState extends ReservationState {
    ApprovedState() {
    }

    /**
     * Cancel reservation
     *
     * @param reservation context reservation
     * @return true
     */
    @Override
    boolean cancel(Reservation reservation) {
        reservation.setState(ReservationStateEnum.CANCELED.getState());
        return true;
    }

    /**
     * Activate reservation
     *
     * @param reservation context reservation
     * @return true
     */
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
