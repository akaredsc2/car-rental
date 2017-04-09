package org.vitaly.model.reservation;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by vitaly on 09.04.17.
 */
public class CanceledStateTest {
    private ReservationState state;
    private Reservation reservation;

    @Before
    public void setUp() throws Exception {
        state = new CanceledState();
        reservation = new Reservation.Builder()
                .setState(state)
                .build();
    }

    @Test
    public void canceledReservationCannotChangeState() throws Exception {
        boolean canChangeState = state.canApprove()
                || state.canCancel()
                || state.canReject()
                || state.canPickUp()
                || state.canDropOff();

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