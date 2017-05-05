package org.vitaly.model.car;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-28.
 */
public class ServedStateTest {
    private CarState state = new ServedState();
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
    public void serveDoesChangeCarStateToServed() throws Exception {
        state.serve(car);

        assertThat(car.getState(), equalTo(state));
    }

    @Test
    public void doReturnDoesChangeCarStateToReturned() throws Exception {
        state.doReturn(car);

        assertThat(car.getState(), allOf(
                not(equalTo(state)),
                instanceOf(ReturnedState.class)));
    }

    @Test
    public void makeUnavailableDoesNotChangeCarState() throws Exception {
        state.makeUnavailable(car);

        assertThat(car.getState(), equalTo(state));
    }
}