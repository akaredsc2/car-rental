package org.vitaly.model.reservation;

/**
 * Canceled reservation state
 */
public class CanceledState extends ReservationState {
    CanceledState() {
    }

    @Override
    public String toString() {
        return "canceled";
    }
}
