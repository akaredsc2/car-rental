package org.vitaly.model.reservation;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by vitaly on 09.04.17.
 */
public class NewStateTest {
    private ReservationState state = new NewState();
    private Reservation reservation = new Reservation.Builder()
            .setState(state)
            .build();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void newStateCanOnlyBecomeApprovedOrRejected() throws Exception {
        boolean canChangeState = state.canApprove()
                && !state.canCancel()
                && state.canReject()
                && !state.canPickUp()
                && !state.canDropOff();

        assertTrue(canChangeState);
    }

    @Test
    public void approveChangesReservationState() throws Exception {
        state.approve(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, allOf(
                not(equalTo(state)),
                instanceOf(ApprovedState.class)));
    }

    @Test
    public void rejectChangesReservationState() throws Exception {
        state.reject(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, allOf(
                not(equalTo(state)),
                instanceOf(RejectedState.class)));
    }

    @Test
    public void cancelDoesNotChangeReservationState() throws Exception {
        state.cancel(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }

    @Test
    public void pickUpDoesNotChangeReservationState() throws Exception {
        state.pickUp(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }

    @Test
    public void dropOffDoesNotChangeReservationState() throws Exception {
        state.dropOff(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }
}