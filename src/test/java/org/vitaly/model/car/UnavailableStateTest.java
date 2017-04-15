package org.vitaly.model.car;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by vitaly on 2017-03-28.
 */
public class UnavailableStateTest {
    private CarState state = new UnavailableState();
    private Car car = new Car.Builder()
            .setState(state)
            .build();

    @Test
    public void unavailableCarCanOnlyBecomeAvailableAndMaintained() throws Exception {
        boolean canChangeState =
                state.canMakeAvailable()
                        && !state.canReserve()
                        && !state.canServe()
                        && !state.canReturn()
                        && !state.canMakeUnavailable()
                        && state.canMaintain();

        assertTrue(canChangeState);
    }

    @Test
    public void makeAvailableDoesChangeCarStateToAvailable() throws Exception {
        state.makeAvailable(car);

        assertThat(car.getState(), allOf(
                not(equalTo(state)),
                instanceOf(AvailableState.class)));
    }

    @Test
    public void reserveDoesNotChangeCarState() throws Exception {
        state.reserve(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void serveDoesChangeCarStateToServed() throws Exception {
        state.serve(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void doReturnDoesNotChangeCarState() throws Exception {
        state.doReturn(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void makeUnavailableDoesChangeCarStateToUnavailable() throws Exception {
        state.makeUnavailable(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void maintainDoesChangeCarStateToMaintained() throws Exception {
        state.maintain(car);

        assertThat(car.getState(), allOf(
                not(equalTo(state)),
                instanceOf(MaintainedState.class)));
    }
}