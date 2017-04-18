package org.vitaly.dao.impl.mysql.mapper;

import org.junit.Test;
import org.vitaly.data.TestData;
import org.vitaly.model.location.Location;

import java.sql.ResultSet;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-15.
 */
public class LocationMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<Location> mapper = new LocationMapper();

    @Test
    public void mapCorrectlySetsLocationParameters() throws Exception {
        Location expectedLocation = TestData.getInstance().getLocation("location1");

        when(resultSet.getLong(LOCATION_LOCATION_ID)).thenReturn(expectedLocation.getId());
        when(resultSet.getString(LOCATION_STATE)).thenReturn(expectedLocation.getState());
        when(resultSet.getString(LOCATION_CITY)).thenReturn(expectedLocation.getCity());
        when(resultSet.getString(LOCATION_STREET)).thenReturn(expectedLocation.getStreet());
        when(resultSet.getString(LOCATION_BUILDING)).thenReturn(expectedLocation.getBuilding());

        Location actualLocation = mapper.map(resultSet);

        assertThat(actualLocation, allOf(
                equalTo(expectedLocation),
                hasId(expectedLocation.getId())));
    }
}