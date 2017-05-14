package org.vitaly.model.reservation;

/**
 * Closed reservation state
 */
public class ClosedState extends ReservationState {
    ClosedState() {
    }

    @Override
    public String toString() {
        return "closed";
    }
}
