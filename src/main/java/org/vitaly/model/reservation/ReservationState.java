package org.vitaly.model.reservation;

/**
 * Created by vitaly on 2017-04-08.
 */
public abstract class ReservationState {
    boolean approve(Reservation reservation) {
        return false;
    }

    boolean reject(Reservation reservation) {
        return false;
    }

    boolean cancel(Reservation reservation) {
        return false;
    }

    boolean activate(Reservation reservation) {
        return false;
    }

    boolean close(Reservation reservation) {
        return false;
    }

    boolean canApprove() {
        return false;
    }

    boolean canReject() {
        return false;
    }

    boolean canCancel() {
        return false;
    }

    boolean canActivate() {
        return false;
    }

    boolean canClose() {
        return false;
    }
}
