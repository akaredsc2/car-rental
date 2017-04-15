package org.vitaly.model.notification;

import org.junit.Test;

/**
 * Created by vitaly on 2017-04-07.
 */
public class BuilderTest {
    private Notification.Builder builder = new Notification.Builder();

    @Test(expected = IllegalArgumentException.class)
    public void setNullCreationDateTimeShouldThrowException() throws Exception {
        builder.setCreationDateTime(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullStatusShouldThrowException() throws Exception {
        builder.setStatus(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullHeaderShouldThrowException() throws Exception {
        builder.setHeader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullContentShouldThrowException() throws Exception {
        builder.setContent(null);
    }
}