package org.vitaly.model.car;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-28.
 */
public class ReturnedStateTest {
    private CarState state = new ReturnedState();
    private Car car = new Car.Builder()
            .setState(state)
            .build();

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