package org.vitaly.model.car;

import org.junit.Test;

/**
 * Created by vitaly on 2017-03-28.
 */
public class CarBuilderTest {
    private Car.Builder builder = new Car.Builder();

    @Test(expected = IllegalArgumentException.class)
    public void setNullStateShouldThrowException() throws Exception {
        builder.setState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullModelShouldThrowException() throws Exception {
        builder.setCarModel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullRegistrationPlateShouldThrowException() throws Exception {
        builder.setRegistrationPlate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullColorShouldThrowException() throws Exception {
        builder.setColor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullPricePerDayShouldThrowException() throws Exception {
        builder.setPricePerDay(null);
    }
}