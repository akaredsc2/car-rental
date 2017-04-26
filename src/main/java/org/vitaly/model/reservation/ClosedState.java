package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class ClosedState extends ReservationState {
    ClosedState() {
    }

    @Override
    public String toString() {
        return "closed";
    }
}
