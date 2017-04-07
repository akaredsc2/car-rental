package org.vitaly.model.notification;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vitaly on 2017-04-07.
 */
public class BuilderTest {
    private Notification.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = new Notification.Builder();
    }

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