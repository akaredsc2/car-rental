package org.vitaly.model.bill;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vitaly on 09.04.17.
 */
public class BuilderTest {
    private Bill.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = new Bill.Builder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullDescriptionShouldThrowException() throws Exception {
        builder.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullCashAmountShouldThrowException() throws Exception {
        builder.setCashAmount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullCreationDateTimeShouldThrowException() throws Exception {
        builder.setCreationDateTime(null);
    }

}