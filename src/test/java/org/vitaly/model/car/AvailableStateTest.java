package org.vitaly.model.car;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by vitaly on 2017-03-28.
 */
public class AvailableStateTest {
    private CarState state = new AvailableState();
    private Car car = new Car.Builder()
            .setState(state)
            .build();

    @Test
    public void makeAvailableDoesNotChangeCarState() throws Exception {
        state.makeAvailable(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void reserveDoesChangeCarStateToReserved() throws Exception {
        state.reserve(car);

        assertThat(car.getState(), allOf(
                not(equalTo(state)),
                instanceOf(ReservedState.class)));
    }

    @Test
    public void serveDoesNotChangeCarState() throws Exception {
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

        assertThat(car.getState(), allOf(
                not(equalTo(state)),
                instanceOf(UnavailableState.class)));
    }
}