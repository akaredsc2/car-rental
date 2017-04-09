package org.vitaly.model.reservation;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by vitaly on 09.04.17.
 */
public class ApprovedStateTest {
    private ReservationState state;
    private Reservation reservation;

    @Before
    public void setUp() throws Exception {
        state = new ApprovedState();
        reservation = new Reservation.Builder()
                .setState(state)
                .build();
    }

    @Test
    public void approvedStateCanOnlyBecomeCanceledOrPickedUp() throws Exception {
        boolean canChangeState = !state.canApprove()
                && state.canCancel()
                && !state.canReject()
                && state.canPickUp()
                && !state.canDropOff();

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
    public void cancelChangesReservationState() throws Exception {
        state.cancel(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, allOf(
                not(equalTo(state)),
                instanceOf(CanceledState.class)));
    }

    @Test
    public void pickUpChangesReservationState() throws Exception {
        state.pickUp(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, allOf(
                not(equalTo(state)),
                instanceOf(PickedUpState.class)));
    }

    @Test
    public void dropOffDoesNotChangeReservationState() throws Exception {
        state.dropOff(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }
}