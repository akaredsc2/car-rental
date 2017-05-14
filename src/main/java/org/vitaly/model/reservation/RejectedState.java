package org.vitaly.model.reservation;

/**
 * Rejected reservation state
 */
public class RejectedState extends ReservationState {
    RejectedState() {
    }

    @Override
    public String toString() {
        return "rejected";
    }
}
