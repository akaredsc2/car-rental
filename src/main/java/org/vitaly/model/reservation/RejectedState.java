package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class RejectedState extends ReservationState {
    RejectedState() {
    }

    @Override
    public String toString() {
        return "rejected";
    }
}
