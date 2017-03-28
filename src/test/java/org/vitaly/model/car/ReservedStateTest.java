package org.vitaly.model.car;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by vitaly on 2017-03-28.
 */
public class ReservedStateTest {
    private CarState state;
    private Car car;

    @Before
    public void setUp() throws Exception {
        state = new ReservedState();
        car = new Car.Builder()
                .setId(1L)
                .setState(state)
                .setModel("model")
                .setRegistrationPlate("aa 001 aa")
                .setColor("orange")
                .setPhotoUrl("none")
                .setPricePerDay(BigDecimal.ONE)
                .build();
    }

    @Test
    public void reservedCarCanOnlyBecomeServed() throws Exception {
        boolean canChangeState =
                !state.canMakeAvailable()
                        && !state.canReserve()
                        && state.canServe()
                        && !state.canReturn()
                        && !state.canMakeUnavailable()
                        && !state.canMaintain();

        assertTrue(canChangeState);
    }

    @Test
    public void makeAvailableDoesNotChangeCarState() throws Exception {
        state.makeAvailable(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void reserveDoesNotChangeCarState() throws Exception {
        state.reserve(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void serveDoesChangeCarStateToServed() throws Exception {
        state.serve(car);

        assertThat(car.getState(), allOf(
                not(equalTo(state)),
                instanceOf(ServedState.class)));
    }

    @Test
    public void doReturnDoesNotChangeCarState() throws Exception {
        state.doReturn(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void makeUnavailableDoesNotChangeCarState() throws Exception {
        state.makeUnavailable(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void maintainDoesNotChangeCarState() throws Exception {
        state.maintain(car);

        assertThat(car.getState(), equalTo(state));
    }
}