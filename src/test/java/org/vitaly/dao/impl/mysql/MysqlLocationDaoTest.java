package org.vitaly.dao.impl.mysql;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.car.Car;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.location.Location;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.CarDtoMapper;
import org.vitaly.service.impl.dtoMapper.CarModelDtoMapper;

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
    static {
        MysqlConnectionPool.configureConnectionPool(MysqlConnectionPool.TEST_CONNECTION_PROPERTIES);
    }

    private static PooledConnection connection = MysqlConnectionPool.getInstance().getConnection();

    private LocationDao locationDao = new MysqlLocationDao(connection);
    private Location location1 = TestData.getInstance().getLocation("location1");
    private Location location2 = TestData.getInstance().getLocation("location2");

    @After
    public void tearDown() throws Exception {
        connection.rollback();
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

        boolean creatingDuplicateEntryResult = locationDao.create(location1).isPresent();

        assertFalse(creatingDuplicateEntryResult);
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
        CarModel carModel = TestData.getInstance().getCarModel("carModel1");
        CarModelDao carModelDao = new MysqlCarModelDao(connection);
        carModel = TestUtil.createEntityWithId(carModel, carModelDao);

        CarModelDtoMapper carModelDtoMapper = new CarModelDtoMapper();
        CarModelDto carModelDto = carModelDtoMapper.mapEntityToDto(carModel);

        Car car = TestData.getInstance().getCar("car1");

        CarDtoMapper carDtoMapper = new CarDtoMapper();
        CarDto carDto = carDtoMapper.mapEntityToDto(car);
        carDto.setCarModelDto(carModelDto);
        car = carDtoMapper.mapDtoToEntity(carDto);

        CarDao carDao = new MysqlCarDao(connection);
        car = TestUtil.createEntityWithId(car, carDao);

        location1 = TestUtil.createEntityWithId(location1, locationDao);
        carDao.moveCarToLocation(car.getId(), location1.getId());

        Location foundLocation = locationDao.findLocationByCarId(car.getId()).orElseThrow(AssertionError::new);

        assertEquals(foundLocation, location1);
    }

    @Test
    public void findLocationOfNonExistingCarEmptyOptional() throws Exception {
        boolean isFound = locationDao.findLocationByCarId(-1).isPresent();

        assertFalse(isFound);
    }

    @Test
    public void changingPhotoUrlOfExistingLocationToNotNull() throws Exception {
        String newPhotoUrl = "photoUrl";

        Location locationWithIdBeforeChange = TestUtil.createEntityWithId(location1, locationDao);
        String photoUrlBeforeChange = locationWithIdBeforeChange.getPhotoUrl();
        locationDao.changeImageUrl(locationWithIdBeforeChange.getId(), newPhotoUrl);

        Location locationAfterChange = locationDao.findById(locationWithIdBeforeChange.getId())
                .orElseThrow(AssertionError::new);

        String photoUrlAfterChange = locationAfterChange.getPhotoUrl();

        assertThat(photoUrlAfterChange, allOf(
                not(equalTo(photoUrlBeforeChange)),
                equalTo(newPhotoUrl)));
    }

    @Test
    public void changingPhotoUrlOfExistingLocationToNotNullReturnsTrue() throws Exception {
        String newPhotoUrl = "photoUrl";

        Location locationWithIdBeforeChange = TestUtil.createEntityWithId(location1, locationDao);
        boolean changeResult = locationDao.changeImageUrl(locationWithIdBeforeChange.getId(), newPhotoUrl);

        assertTrue(changeResult);
    }

    @Test
    public void changingPhotoUrlOfExistingLocationToNull() throws Exception {
        Location locationWithIdBeforeChange = TestUtil.createEntityWithId(location1, locationDao);
        locationDao.changeImageUrl(locationWithIdBeforeChange.getId(), null);

        Location locationAfterChange = locationDao.findById(locationWithIdBeforeChange.getId())
                .orElseThrow(AssertionError::new);

        String photoUrlAfterChange = locationAfterChange.getPhotoUrl();

        assertThat(photoUrlAfterChange, nullValue());
    }

    @Test
    public void changingPhotoUrlOfExistingLocationToNullReturnsTrue() throws Exception {
        Location locationWithIdBeforeChange = TestUtil.createEntityWithId(location1, locationDao);
        boolean changeResult = locationDao.changeImageUrl(locationWithIdBeforeChange.getId(), null);

        assertTrue(changeResult);
    }

    @Test
    public void changingPhotoUrlOfNonExistingLocationReturnsFalse() throws Exception {
        String newPhotoUrl = "photoUrl";

        boolean changeResult = locationDao.changeImageUrl(-1, newPhotoUrl);

        assertFalse(changeResult);
    }
}