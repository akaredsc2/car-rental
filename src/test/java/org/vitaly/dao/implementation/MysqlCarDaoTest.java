package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.location.Location;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Created by vitaly on 2017-03-27.
 */
public class MysqlCarDaoTest {
    private static final String LOCATION_CLEAN_UP_QUERY = "delete from location";
    private static final String CAR_CLEAN_UP_QUERY = "delete from car";
    private static MysqlConnectionPool pool;
    private static DaoFactory factory;

    private Car car1;
    private Car car2;
    private Location location1;
    private PooledConnection connection;
    private CarDao carDao;
    private LocationDao locationDao;

    @BeforeClass
    public static void initPoolAndFactory() {
        pool = MysqlConnectionPool.getTestInstance();
        factory = DaoFactory.getMysqlDaoFactory();
    }

    @Before
    public void setUp() throws Exception {
        car1 = new Car.Builder()
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setModel("Ford Focus")
                .setRegistrationPlate("666 satan 666")
                .setPhotoUrl("http://bit.ly/2o8TCb9")
                .setColor("grey")
                .setPricePerDay(BigDecimal.valueOf(100.0))
                .build();
        car2 = new Car.Builder()
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setModel("Ford Fiesta")
                .setRegistrationPlate("777 lucky 777")
                .setPhotoUrl("http://bit.ly/2mHkMc3")
                .setColor("blue")
                .setPricePerDay(BigDecimal.valueOf(120.0))
                .build();
        location1 = new Location.Builder()
                .setState("Kiev region")
                .setCity("Kotsjubinske")
                .setStreet("Ponomarjova")
                .setBuilding("18-a")
                .setCars(new ArrayList<>())
                .build();

        connection = pool.getConnection();
        carDao = factory.createCarDao(connection);
        locationDao = factory.createLocationDao(connection);
    }

    @Test
    public void findByIdExistingCarReturnsCar() throws Exception {
        Long createdId = carDao.create(car1).orElseThrow(AssertionError::new);

        Car foundCar = carDao.findById(createdId).orElseThrow(AssertionError::new);

        assertThat(foundCar, equalTo(car1));
    }

    @Test
    public void findByIdNonExistingCarReturnsEmptyOptional() throws Exception {
        boolean findResult = carDao.findById(-1L).isPresent();

        assertThat(findResult, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByNullIdShouldThrowException() throws Exception {
        carDao.findById(null);
    }

    @Test
    public void findIdOfExistingCarReturnsId() throws Exception {
        Long createdId = carDao.create(car1).orElseThrow(AssertionError::new);

        Long foundId = carDao.findIdOfEntity(car1).orElseThrow(AssertionError::new);

        assertThat(foundId, equalTo(createdId));
    }

    @Test
    public void findIdOfNOnExistingCarReturnsEmptyOptional() throws Exception {
        boolean findResult = carDao.findIdOfEntity(car1).isPresent();

        assertThat(findResult, equalTo(false));
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

        assertThat(creationResult, equalTo(true));
    }

    @Test
    public void failedCreationReturnsEmptyOptional() throws Exception {
        boolean creationResult = carDao.create(car1).isPresent();

        assertThat(creationResult, equalTo(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullShouldThrowException() throws Exception {
        carDao.create(null);
    }

    @Test
    public void updateExistingCarReturnsOne() throws Exception {
        Long createId = carDao.create(car1).orElseThrow(AssertionError::new);

        int updateCount = carDao.update(createId, car2);

        assertThat(updateCount, equalTo(1));
    }

    @Test
    public void updateExistingCarReturnsUpdatesCar() throws Exception {
        Long createId = carDao.create(car1).orElseThrow(AssertionError::new);

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
    public void updateNullIdShouldThrowException() throws Exception {
        carDao.update(null, car1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateIdWithNullCarShouldThrowException() throws Exception {
        carDao.update(1L, null);
    }

    @Test
    public void addExistingCarToExistingLocationReturnsTrue() throws Exception {
        Car createdCar = createCarWithId(car1);
        Location createdLocation = createLocationWithId(location1);

        boolean addResult = carDao.addCarToLocation(createdCar, createdLocation);

        assertThat(addResult, equalTo(true));
    }

    private Car createCarWithId(Car car) {
        Long createdCarId = carDao.create(car).orElseThrow(AssertionError::new);
        return carDao.findById(createdCarId).orElseThrow(AssertionError::new);
    }

    private Location createLocationWithId(Location location) {
        Long createdLocationId = locationDao.create(location).orElseThrow(AssertionError::new);
        return locationDao.findById(createdLocationId).orElseThrow(AssertionError::new);
    }

    @Test
    public void addExistingCarToExistingLocationAddsCarToLocation() throws Exception {
        Car createdCar1 = createCarWithId(car1);
        Car createdCar2 = createCarWithId(car2);
        Location createdLocation = createLocationWithId(location1);

        carDao.addCarToLocation(createdCar1, createdLocation);
        carDao.addCarToLocation(createdCar2, createdLocation);

        List<Car> carsAtLocation = carDao.getCarsAtLocation(createdLocation);

        assertThat(carsAtLocation, hasItems(car1, car2));
    }

    @Test
    public void addNonExistingCarToExistingLocationReturnsFalse() throws Exception {
        makeFakeIdForCar(car1);
        Location location = createLocationWithId(location1);

        boolean addResult = carDao.addCarToLocation(car1, location);

        assertThat(addResult, equalTo(false));
    }

    private void makeFakeIdForCar(Car car) throws NoSuchFieldException, IllegalAccessException {
        Field carIdField = car.getClass().getDeclaredField("id");
        carIdField.setAccessible(true);
        carIdField.set(car, -1L);
    }

    @Test
    public void addExistingCarToNonExistingLocationReturnsFalse() throws Exception {
        Car createdCar = createCarWithId(car1);
        makeFakeIdForLocation(location1);

        boolean addResult = carDao.addCarToLocation(createdCar, location1);

        assertThat(addResult, equalTo(false));
    }

    private void makeFakeIdForLocation(Location location) throws NoSuchFieldException, IllegalAccessException {
        Field locationIdField = location.getClass().getDeclaredField("id");
        locationIdField.setAccessible(true);
        locationIdField.set(location, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullCarToLocationShouldThrowException() throws Exception {
        carDao.addCarToLocation(null, location1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCarToNullLocationShouldThrowException() throws Exception {
        carDao.addCarToLocation(car1, null);
    }

    @Test
    public void getCarsAtExistingLocationWithNoCarsReturnsEmptyList() throws Exception {
        Location location = createLocationWithId(location1);

        List<Car> cars = carDao.getCarsAtLocation(location);

        assertThat(cars, empty());
    }

    @Test
    public void getCarsAtNonExistingLocationReturnsEmptyList() throws Exception {
        makeFakeIdForLocation(location1);

        List<Car> cars = carDao.getCarsAtLocation(location1);

        assertThat(cars, empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCarsFromNullLocationShouldThrowException() throws Exception {
        carDao.getCarsAtLocation(null);
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(CAR_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.prepareStatement(LOCATION_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }
}