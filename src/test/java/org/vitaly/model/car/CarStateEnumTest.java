package org.vitaly.model.car;

import junit.framework.AssertionFailedError;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-05-04.
 */
public class CarStateEnumTest {
    @Test
    public void stateOfStringThatIsPartOfEnumReturnsOptionalWithCorrespondingState() throws Exception {
        CarState carState = new AvailableState();
        String stateString = carState.toString();

        CarState actualState = CarStateEnum.stateOf(stateString).orElseThrow(AssertionFailedError::new);

        assertThat(actualState, instanceOf(carState.getClass()));
    }

    @Test
    public void stateOfStringThatNotIsPartOfEnumReturnsEmptyOptional() throws Exception {
        String stateString = "random";

        boolean isPresent = CarStateEnum.stateOf(stateString).isPresent();

        assertFalse(isPresent);
    }
}