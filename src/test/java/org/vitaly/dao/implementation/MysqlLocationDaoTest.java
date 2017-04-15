package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.car.Car;
import org.vitaly.model.location.Location;

import java.util.List;

import static junit.framework.TestCase.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 2017-03-26.
 */
public class MysqlLocationDaoTest {
    private static final String CLEAN_UP_QUERY = "delete from location";
    private static final String CAR_CLEAN_UP_QUERY = "delete from car";

    private static PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();
    private static LocationDao locationDao = DaoFactory.getMysqlDaoFactory().createLocationDao(connection);

    private Location location1 = TestData.getInstance().getLocation("location1");
    private Location location2 = TestData.getInstance().getLocation("location2");

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        connection.close();
    }

    @Test
    public void findByIdExistingLocationReturnsLocation() throws Exception {
        long createdLocationId = locationDao.create(location1).orElseThrow(AssertionError::new);

        Location foundLocation = locationDao.findById(createdLocationId).orElseThrow(AssertionError::new);

        assertThat(foundLocation, equalTo(location1));
    }

    @Test
    public void findByIdNonExistingLocationReturnsEmptyOptional() throws Exception {
        locationDao.create(location1).orElseThrow(AssertionError::new);

        boolean findResult = locationDao.findById(-1L).isPresent();

        assertFalse(findResult);
    }

    @Test
    public void successfulFindIdOfLocationReturnsId() throws Exception {
        Location createdLocation = TestUtil.createEntityWithId(location1, locationDao);

        long foundId = locationDao.findIdOfEntity(location1).orElseThrow(AssertionError::new);

        assertThat(createdLocation, hasId(foundId));
    }

    @Test
    public void findIdOfNonExistingLocationReturnsEmptyOptional() throws Exception {
        boolean findResult = locationDao.findIdOfEntity(location2).isPresent();

        assertFalse(findResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIdOfNullLocationShouldThrowException() throws Exception {
        locationDao.findIdOfEntity(null);
    }

    @Test
    public void getAllContainsAllCreatedLocations() throws Exception {
        locationDao.create(location1);
        locationDao.create(location2);

        List<Location> locationList = locationDao.getAll();

        assertThat(locationList, hasItems(location2, location1));
    }

    @Test
    public void getAllOnEmptyLocationsReturnsEmptyList() throws Exception {
        List<Location> locationList = locationDao.getAll();

        assertThat(locationList, empty());
    }

    @Test
    public void successfulCreationReturnsId() throws Exception {
        boolean creationResult = locationDao.create(location1).isPresent();

        assertTrue(creationResult);
    }

    @Test
    public void creatingLocationWithSameStateCityStreetAndBuildingReturnsEmptyOptional() throws Exception {
        locationDao.create(location1);
        boolean creationResult = locationDao.create(location1).isPresent();

        assertFalse(creationResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingNullEntityShouldThrowException() throws Exception {
        locationDao.create(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateIsUnsupportedForLocationDao() throws Exception {
        locationDao.update(1L, location1);
    }

    @Test
    public void findLocationOfExistingCarReturnsLocation() throws Exception {
        Car car = TestData.getInstance().getCar("car1");
        CarDao carDao = MysqlDaoFactory.getInstance().createCarDao(connection);
        car = TestUtil.createEntityWithId(car, carDao);

        location1 = TestUtil.createEntityWithId(location1, locationDao);
        carDao.addCarToLocation(car.getId(), location1.getId());

        Location foundLocation = locationDao.findLocationByCarId(car.getId()).orElseThrow(AssertionError::new);

        connection.initializeTransaction();
        connection.prepareStatement(CAR_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();

        assertEquals(foundLocation, location1);
    }

    @Test
    public void findLocationOfNonExistingCarEmptyOptional() throws Exception {
        boolean isFound = locationDao.findLocationByCarId(-1).isPresent();

        assertFalse(isFound);
    }
}