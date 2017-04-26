package org.vitaly.model.reservation;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by vitaly on 09.04.17.
 */
public class ActiveStateTest {
    private ReservationState state = new ActiveState();
    private Reservation reservation = new Reservation.Builder()
            .setState(state)
            .build();

    @Test
    public void activeStateCanOnlyBecomeClosed() throws Exception {
        boolean canChangeState = !state.canApprove()
                && !state.canCancel()
                && !state.canReject()
                && !state.canActivate()
                && state.canClose();

        assertTrue(canChangeState);
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
    public void cancelDoesNotChangesReservationState() throws Exception {
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
    public void dropOffChangesReservationState() throws Exception {
        state.close(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, allOf(
                not(equalTo(state)),
                instanceOf(ClosedState.class)));
    }
}