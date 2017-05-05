package org.vitaly.model.car;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-28.
 */
public class ReservedStateTest {
    private CarState state = new ReservedState();
    private Car car = new Car.Builder()
            .setState(state)
            .build();

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
}