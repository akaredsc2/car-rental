package org.vitaly.model.reservation;

/**
 * Abstract reservation state. Part of State design pattern. Use reservation state enum
 * to access instances of different states.
 *
 * @see Reservation
 */
public abstract class ReservationState {

    /**
     * Try to approve reservation
     *
     * @param reservation context reservation
     * @return true if reservation was approved, false otherwise
     */
    boolean approve(Reservation reservation) {
        return false;
    }

    /**
     * Try to reject reservation
     *
     * @param reservation context reservation
     * @return true if reservation was rejected, false otherwise
     */
    boolean reject(Reservation reservation) {
        return false;
    }

    /**
     * Try to cancel reservation
     *
     * @param reservation context reservation
     * @return true if reservation was canceled, false otherwise
     */
    boolean cancel(Reservation reservation) {
        return false;
    }

    /**
     * Try to activate reservation
     *
     * @param reservation context reservation
     * @return true if reservation was activated, false otherwise
     */
    boolean activate(Reservation reservation) {
        return false;
    }

    /**
     * Try to close reservation
     *
     * @param reservation context reservation
     * @return true if reservation was closed, false otherwise
     */
    boolean close(Reservation reservation) {
        return false;
    }
}
