package org.vitaly.model.reservation;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by vitaly on 09.04.17.
 */
public class PickedUpStateTest {
    private ReservationState state = new PickedUpState();
    private Reservation reservation = new Reservation.Builder()
            .setState(state)
            .build();

    @Test
    public void pickedUpStateCanOnlyBecomeDroppedOff() throws Exception {
        boolean canChangeState = !state.canApprove()
                && !state.canCancel()
                && !state.canReject()
                && !state.canPickUp()
                && state.canDropOff();

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
    public void pickUpDoesNotChangeReservationState() throws Exception {
        state.pickUp(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, equalTo(state));
    }

    @Test
    public void dropOffChangesReservationState() throws Exception {
        state.dropOff(reservation);
        ReservationState afterChange = reservation.getState();

        assertThat(afterChange, allOf(
                not(equalTo(state)),
                instanceOf(DroppedOffState.class)));
    }
}