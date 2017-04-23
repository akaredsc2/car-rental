package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public class CanceledState extends ReservationState {
    CanceledState() {
    }

    @Override
    public String toString() {
        return "canceled";
    }
}
