package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.model.location.Location;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Created by vitaly on 2017-03-26.
 */
public class MysqlLocationDaoTest {
    private static final String CLEAN_UP_QUERY = "delete from location";
    private static MysqlConnectionPool pool;
    private static DaoFactory factory;

    private Location location1;
    private Location location2;
    private PooledConnection connection;
    private LocationDao locationDao;

    @BeforeClass
    public static void initPoolAndFactory() {
        pool = MysqlConnectionPool.getTestInstance();
        factory = DaoFactory.getMysqlDaoFactory();
    }

    @Before
    public void setUp() throws Exception {
        connection = pool.getConnection();
        locationDao = factory.createLocationDao(connection);

        location1 = new Location.Builder()
                .setState("Kiev region")
                .setCity("Kotsjubinske")
                .setStreet("Ponomarjova")
                .setBuilding("18-a")
                .setCars(new ArrayList<>())
                .build();
        location2 = new Location.Builder()
                .setState("Odesska")
                .setCity("Odesa")
                .setStreet("Peysivska")
                .setBuilding("14")
                .setCars(new ArrayList<>())
                .build();
    }

    @Test
    public void findByIdExistingLocationReturnsLocation() throws Exception {
        Long createdLocationId = locationDao.create(location1).orElseThrow(AssertionError::new);

        Location foundLocation = locationDao.findById(createdLocationId).orElseThrow(AssertionError::new);

        assertThat(foundLocation, equalTo(location1));
    }

    @Test
    public void findByIdNonExistingLocationReturnsEmptyOptional() throws Exception {
        locationDao.create(location1).orElseThrow(AssertionError::new);

        boolean findResult = locationDao.findById(-1L).isPresent();

        assertThat(findResult, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByNullIdShouldThrowException() throws Exception {
        locationDao.findById(null);
    }

    @Test
    public void successfulFindIdOfEntityReturnsCorrectId() throws Exception {
        Long createdId = locationDao.create(location1).orElseThrow(AssertionError::new);

        Long foundId = locationDao.findIdOfEntity(location1).orElseThrow(AssertionError::new);

        assertThat(foundId, equalTo(createdId));
    }

    @Test
    public void findIdOfNonExistingEntityShouldThrowException() throws Exception {
        boolean findResult = locationDao.findIdOfEntity(location2).isPresent();

        assertThat(findResult, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIdOfNullEntityShouldThrowException() throws Exception {
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

        assertThat(creationResult, equalTo(true));
    }

    @Test
    public void failedCreationReturnsEmptyOptional() throws Exception {
        locationDao.create(location1);
        boolean creationResult = locationDao.create(location1).isPresent();

        assertThat(creationResult, equalTo(false));
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
    public void getLocationCountForEmptyTableReturnsZero() throws Exception {
        int locationCount = locationDao.getLocationCount();

        assertThat(locationCount, equalTo(0));
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }
}