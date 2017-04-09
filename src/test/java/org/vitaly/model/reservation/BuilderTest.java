package org.vitaly.model.reservation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vitaly on 09.04.17.
 */
public class BuilderTest {
    private Reservation.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = new Reservation.Builder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullClientShouldThrowException() throws Exception {
        builder.setClient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullCarShouldThrowException() throws Exception {
        builder.setCar(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullPickUpDatetimeShouldThrowException() throws Exception {
        builder.setPickUpDatetime(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullDropOffDatetimeShouldThrowException() throws Exception {
        builder.setDropOffDatetime(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullStateShouldThrowException() throws Exception {
        builder.setState(null);
    }
}