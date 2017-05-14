package org.vitaly.model.reservation;

/**
 * Active reservation state
 *
 * @see ReservationState
 */
public class ActiveState extends ReservationState {
    ActiveState() {
    }

    /**
     * Closing reservation
     *
     * @param reservation context reservation
     * @return true
     */
    @Override
    boolean close(Reservation reservation) {
        reservation.setState(ReservationStateEnum.CLOSED.getState());
        return true;
    }

    @Override
    public String toString() {
        return "active";
    }
}
