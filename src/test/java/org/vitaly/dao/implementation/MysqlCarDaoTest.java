package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.implementation.connectionPool.MysqlConnectionPool;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.UnavailableState;
import org.vitaly.model.location.Location;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 2017-03-27.
 */
public class MysqlCarDaoTest {
    private static final String LOCATION_CLEAN_UP_QUERY = "delete from location";

    private static PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();
    private static CarDao carDao = new MysqlCarDao(connection);
    private static Location location;

    private Car car1 = TestData.getInstance().getCar("car1");
    private Car car2 = TestData.getInstance().getCar("car2");

    @BeforeClass
    public static void init() throws Exception {
        Location location1 = TestData.getInstance().getLocation("location1");
        LocationDao locationDao = new MysqlLocationDao(connection);
        MysqlCarDaoTest.location = TestUtil.createEntityWithId(location1, locationDao);

        connection.commit();
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        connection.prepareStatement(LOCATION_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }

    @Test
    public void findByIdExistingCarReturnsCar() throws Exception {
        long createdId = carDao.create(car1).orElseThrow(AssertionError::new);

        Car foundCar = carDao.findById(createdId).orElseThrow(AssertionError::new);

        assertThat(foundCar, equalTo(car1));
    }

    @Test
    public void findByIdNonExistingCarReturnsEmptyOptional() throws Exception {
        boolean findResult = carDao.findById(-1L).isPresent();

        assertFalse(findResult);
    }

    @Test
    public void findIdOfExistingCarReturnsId() throws Exception {
        Car createdCar = TestUtil.createEntityWithId(car1, carDao);

        long foundId = carDao.findIdOfEntity(car1).orElseThrow(AssertionError::new);

        assertThat(createdCar, hasId(foundId));
    }

    @Test
    public void findIdOfNOnExistingCarReturnsEmptyOptional() throws Exception {
        boolean findResult = carDao.findIdOfEntity(car1).isPresent();

        assertFalse(findResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIdOfNullShouldThrowException() throws Exception {
        carDao.findIdOfEntity(null);
    }

    @Test
    public void getAllContainsAllCreatedCars() throws Exception {
        carDao.create(car1);
        carDao.create(car2);

        List<Car> carList = carDao.getAll();

        assertThat(carList, hasItems(car1, car2));
    }

    @Test
    public void getAllOnEmptyCarsReturnsEmptyList() throws Exception {
        List<Car> carList = carDao.getAll();

        assertThat(carList, empty());
    }

    @Test
    public void successfulCreationReturnsId() throws Exception {
        boolean creationResult = carDao.create(car1).isPresent();

        assertTrue(creationResult);
    }

    @Test(expected = DaoException.class)
    public void creatingCarWithSameRegistrationPlateShouldThrowException() throws Exception {
        carDao.create(car1);
        carDao.create(car1);
    }

    @Test
    public void createdCarHasUnavailableState() throws Exception {
        Car car = TestUtil.createEntityWithId(car1, carDao);

        assertThat(car.getState(), instanceOf(UnavailableState.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullShouldThrowException() throws Exception {
        carDao.create(null);
    }

    @Test
    public void updateExistingCarReturnsOne() throws Exception {
        long createId = carDao.create(car1).orElseThrow(AssertionError::new);

        int updateCount = carDao.update(createId, car2);

        assertThat(updateCount, equalTo(1));
    }

    @Test
    public void updateExistingCarReturnsUpdatesCar() throws Exception {
        long createId = carDao.create(car1).orElseThrow(AssertionError::new);

        carDao.update(createId, car2);

        Car updatedCar = carDao.findById(createId).orElseThrow(AssertionError::new);
        assertThat(updatedCar, equalTo(car2));
    }

    @Test
    public void updateNonExistingCarReturnsZero() throws Exception {
        int updateCount = carDao.update(-1L, car2);

        assertThat(updateCount, equalTo(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateIdWithNullCarShouldThrowException() throws Exception {
        carDao.update(1L, null);
    }

    @Test
    public void addExistingCarToExistingLocationReturnsTrue() throws Exception {
        Car createdCar = TestUtil.createEntityWithId(car1, carDao);

        boolean addResult = carDao.addCarToLocation(createdCar.getId(), location.getId());

        assertTrue(addResult);
    }

    @Test
    public void addExistingCarToExistingLocationAddsCarToLocation() throws Exception {
        Car createdCar1 = TestUtil.createEntityWithId(car1, carDao);
        Car createdCar2 = TestUtil.createEntityWithId(car2, carDao);

        carDao.addCarToLocation(createdCar1.getId(), location.getId());
        carDao.addCarToLocation(createdCar2.getId(), location.getId());

        List<Car> carsAtLocation = carDao.findCarsAtLocation(location.getId());

        assertThat(carsAtLocation, hasItems(car1, car2));
    }

    @Test
    public void addNonExistingCarToExistingLocationReturnsFalse() throws Exception {
        boolean addResult = carDao.addCarToLocation(-1, location.getId());

        assertFalse(addResult);
    }

    @Test(expected = DaoException.class)
    public void addExistingCarToNonExistingLocationShouldThrowException() throws Exception {
        Car createdCar = TestUtil.createEntityWithId(car1, carDao);

        carDao.addCarToLocation(createdCar.getId(), -1);
    }

    @Test
    public void getCarsAtExistingLocationWithNoCarsReturnsEmptyList() throws Exception {
        List<Car> cars = carDao.findCarsAtLocation(location.getId());

        assertThat(cars, empty());
    }

    @Test
    public void getCarsAtNonExistingLocationReturnsEmptyList() throws Exception {
        List<Car> cars = carDao.findCarsAtLocation(-1);

        assertThat(cars, empty());
    }

    @Test
    public void findCarsByExistingModelReturnsAllMatchingCars() throws Exception {
        car1 = TestUtil.createEntityWithId(car1, carDao);
        car2 = TestUtil.createEntityWithId(car2, carDao);

        String targetModel = car1.getModel();

        List<Car> matchingCars = carDao.findCarsByModel(targetModel);

        assertThat(matchingCars, allOf(
                hasItem(car1),
                not(hasItem(car2))));
    }

    @Test
    public void findCarsByNonExistingModelReturnsEmptyList() throws Exception {
        car1 = TestUtil.createEntityWithId(car1, carDao);
        car2 = TestUtil.createEntityWithId(car2, carDao);

        String targetModel = "tesla model s";

        List<Car> matchingCars = carDao.findCarsByModel(targetModel);

        assertThat(matchingCars, empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findCarsByNullModelShouldThrowException() throws Exception {
        carDao.findCarsByModel(null);
    }

    @Test
    public void findCarsWithPriceBetweenReturnsAllMatchingCars() throws Exception {
        car1 = TestUtil.createEntityWithId(car1, carDao);
        car2 = TestUtil.createEntityWithId(car2, carDao);

        BigDecimal first = car1.getPricePerDay();
        BigDecimal second = car2.getPricePerDay();
        BigDecimal from = first.min(second);
        BigDecimal to = first.max(second).subtract(BigDecimal.ONE);

        List<Car> matchingCars = carDao.findCarsWithPriceBetween(from, to);

        assertThat(matchingCars, allOf(
                iterableWithSize(1),
                hasItem(car1),
                not(hasItem(car2))));
    }

    @Test
    public void findCarsWithPriceBetweenReturnsEmptyListIfThereAreNoMatches() throws Exception {
        BigDecimal from = BigDecimal.TEN.negate();
        BigDecimal to = BigDecimal.ONE.negate();

        List<Car> matchingCars = carDao.findCarsWithPriceBetween(from, to);

        assertThat(matchingCars, empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findCarsWithPriceBetweenNullAndNumberShouldThrowException() throws Exception {
        carDao.findCarsWithPriceBetween(null, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findCarsWithPriceBetweenNumberAndNullShouldThrowException() throws Exception {
        carDao.findCarsWithPriceBetween(BigDecimal.ONE, null);
    }

    @Test
    public void findAllModelsReturnsAllDistinctModels() throws Exception {
        car1 = TestUtil.createEntityWithId(car1, carDao);
        car2 = TestUtil.createEntityWithId(car2, carDao);
        Car car3 = TestUtil.createEntityWithId(TestData.getInstance().getCar("car3"), carDao);

        Set<String> actualModels = new HashSet<>();
        actualModels.add(car1.getModel());
        actualModels.add(car2.getModel());
        actualModels.add(car3.getModel());

        List<String> foundModels = carDao.findAllCarModels();

        assertThat(foundModels, allOf(
                iterableWithSize(2),
                hasItems(actualModels.toArray(new String[0]))));
    }

    @Test
    public void findAllModelsReturnsEmptyListOnEmptyCarTable() throws Exception {
        List<String> foundModels = carDao.findAllCarModels();

        assertThat(foundModels, empty());
    }
}