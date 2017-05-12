package org.vitaly.dao.impl.mysql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.data.TestUtil;
import org.vitaly.model.location.Location;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 2017-03-26.
 */
public class MysqlLocationDaoTest {
    private LocationDao locationDao = new MysqlLocationDao();

    private Location location1 = new Location.Builder()
            .setId(4L)
            .setState("Kiev region")
            .setCity("Kotsjubinske")
            .setStreet("Ponomarjova")
            .setBuilding("18-a")
            .setPhotoUrl("location1url")
            .build();

    private Location location2 = new Location.Builder()
            .setId(5L)
            .setState("Odesska")
            .setCity("Odesa")
            .setStreet("Peysivska")
            .setBuilding("14")
            .setPhotoUrl("location2url")
            .build();

    @Before
    public void setUp() throws Exception {
        TransactionManager.startTransaction();
    }

    @After
    public void tearDown() throws Exception {
        TransactionManager.rollback();
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

        boolean creatingDuplicateEntryResult = locationDao.create(location1).isPresent();

        assertFalse(creatingDuplicateEntryResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingNullEntityShouldThrowException() throws Exception {
        locationDao.create(null);
    }

    @Test
    public void findLocationOfExistingCarReturnsLocation() throws Exception {
        int carId = 1;
        location1 = TestUtil.createEntityWithId(location1, locationDao);

        DaoTemplate.executeInsert(
                "INSERT INTO model (model_id, model_name, doors, seats, horse_powers) " +
                        "VALUES (1, '', 1, 1, 1)", Collections.emptyMap());
        DaoTemplate.executeInsert(
                "INSERT INTO " +
                        "car(car_id, car_status, model_id, registration_plate, color, price_per_day, location_id) " +
                        "VALUES (1, 'available', 1, '', '', 1, ?)", Collections.singletonMap(1, location1.getId()));
        Location foundLocation = locationDao.findLocationByCarId(carId).orElseThrow(AssertionError::new);

        assertEquals(foundLocation, location1);
    }

    @Test
    public void findLocationOfNonExistingCarEmptyOptional() throws Exception {
        boolean isFound = locationDao.findLocationByCarId(-1).isPresent();

        assertFalse(isFound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatingWithNullLocationShouldThrowException() throws Exception {
        locationDao.update(1, null);
    }

    @Test
    public void updatingPhotoUrlOfExistingLocationToNotNull() throws Exception {
        String newPhotoUrl = "photoUrl";
        Location locationWithPhoto = new Location.Builder()
                .setPhotoUrl(newPhotoUrl)
                .build();

        Location locationWithIdBeforeUpdate = TestUtil.createEntityWithId(location1, locationDao);
        String photoUrlBeforeUpdate = locationWithIdBeforeUpdate.getPhotoUrl();
        locationDao.update(locationWithIdBeforeUpdate.getId(), locationWithPhoto);

        Location locationAfterUpdate = locationDao.findById(locationWithIdBeforeUpdate.getId())
                .orElseThrow(AssertionError::new);

        String photoUrlAfterUpdate = locationAfterUpdate.getPhotoUrl();

        assertThat(photoUrlAfterUpdate, allOf(
                not(equalTo(photoUrlBeforeUpdate)),
                equalTo(newPhotoUrl)));
    }

    @Test
    public void updatingPhotoUrlOfExistingLocationToNotNullReturnsTrue() throws Exception {
        String newPhotoUrl = "photoUrl";
        Location locationWithPhoto = new Location.Builder()
                .setPhotoUrl(newPhotoUrl)
                .build();

        Location locationWithIdBeforeChange = TestUtil.createEntityWithId(location1, locationDao);
        int updateResult = locationDao.update(locationWithIdBeforeChange.getId(), locationWithPhoto);

        assertEquals(1, updateResult);
    }

    @Test
    public void updatingPhotoUrlOfExistingLocationToNull() throws Exception {
        Location locationWithIdBeforeChange = TestUtil.createEntityWithId(location1, locationDao);
        Location locationWithoutPhoto = new Location.Builder().build();
        locationDao.update(locationWithIdBeforeChange.getId(), locationWithoutPhoto);

        Location locationAfterUpdate = locationDao.findById(locationWithIdBeforeChange.getId())
                .orElseThrow(AssertionError::new);

        String photoUrlAfterUpdate = locationAfterUpdate.getPhotoUrl();

        assertThat(photoUrlAfterUpdate, nullValue());
    }

    @Test
    public void updatingPhotoUrlOfExistingLocationToNullReturnsOne() throws Exception {
        Location locationWithIdBeforeChange = TestUtil.createEntityWithId(location1, locationDao);
        Location locationWithoutPhoto = new Location.Builder().build();
        int updateResult = locationDao.update(locationWithIdBeforeChange.getId(), locationWithoutPhoto);

        assertEquals(1, updateResult);
    }

    @Test
    public void updatingPhotoUrlOfNonExistingLocationReturnsZero() throws Exception {
        String newPhotoUrl = "photoUrl";
        Location locationWithPhoto = new Location.Builder()
                .setPhotoUrl(newPhotoUrl)
                .build();

        int updateResult = locationDao.update(-1, locationWithPhoto);

        assertEquals(0, updateResult);
    }
}