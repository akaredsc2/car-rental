package org.vitaly.model.bill;

import org.junit.Test;

/**
 * Created by vitaly on 09.04.17.
 */
public class BuilderTest {
    private Bill.Builder builder = new Bill.Builder();

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