package org.vitaly.model.reservation;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 09.04.17.
 */
public class ClosedStateTest {
    private ReservationState state = new ClosedState();
    private Reservation reservation = new Reservation.Builder()
            .setState(state)
            .build();

    @Test
    public void rejectedReservationCannotChangeState() throws Exception {
        boolean canChangeState = state.canApprove()
                || state.canCancel()
                || state.canReject()
                || state.canActivate()
                || state.canClose();

        assertFalse(canChangeState);
    }

    @Test
    public void approveDoesNotChangeReservationState() throws Exception {
        state.approve(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }

    @Test
    public void rejectDoesNotChangeReservationState() throws Exception {
        state.reject(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }

    @Test
    public void cancelDoesNotChangeReservationState() throws Exception {
        state.cancel(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }

    @Test
    public void activateDoesNotChangeReservationState() throws Exception {
        state.activate(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }

    @Test
    public void closeDoesNotChangeReservationState() throws Exception {
        state.close(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }
}