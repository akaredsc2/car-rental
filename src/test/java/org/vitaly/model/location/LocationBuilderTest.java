package org.vitaly.model.location;

import org.junit.Test;

/**
 * Created by vitaly on 2017-03-26.
 */
public class LocationBuilderTest {
    private Location.Builder builder = new Location.Builder();

    @Test(expected = IllegalArgumentException.class)
    public void setNullStateShouldThrowException() throws Exception {
        builder.setState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullCityShouldThrowException() throws Exception {
        builder.setCity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullStreetShouldThrowException() throws Exception {
        builder.setStreet(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullBuildingShouldThrowException() throws Exception {
        builder.setBuilding(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullCarsShouldThrowException() throws Exception {
        builder.setCars(null);
    }
}