package org.vitaly.dao.impl.mysql;

import org.junit.*;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.UnavailableState;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.location.Location;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.CarDtoMapper;
import org.vitaly.service.impl.dtoMapper.CarModelDtoMapper;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.*;
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
    private static final String MODEL_CLEAN_UP_QUERY = "delete from model";

    static {
        MysqlConnectionPool.configureConnectionPool(MysqlConnectionPool.TEST_CONNECTION_PROPERTIES);
    }

    private static PooledConnection connection = MysqlConnectionPool.getInstance().getConnection();
    private static long locationId;
    private static CarModel carModel1;
    private static CarModel carModel2;

    private CarDao carDao = new MysqlCarDao();
    private Car car1 = TestData.getInstance().getCar("car1");
    private Car car2 = TestData.getInstance().getCar("car2");

    @BeforeClass
    public static void init() throws Exception {
        connection.setInTransaction(true);

        Location locationFromTestData = TestData.getInstance().getLocation("location1");
        LocationDao locationDao = new MysqlLocationDao();
        locationId = TestUtil.createEntityWithId(locationFromTestData, locationDao).getId();

        CarModel carModelFromTestData1 = TestData.getInstance().getCarModel("carModel1");
        CarModel carModelFromTestData2 = TestData.getInstance().getCarModel("carModel2");
        CarModelDao carModelDao = new MysqlCarModelDao();
        carModel1 = TestUtil.createEntityWithId(carModelFromTestData1, carModelDao);
        carModel2 = TestUtil.createEntityWithId(carModelFromTestData2, carModelDao);

        connection.commit();
    }

    @Before
    public void setUp() throws Exception {
        CarModelDtoMapper carModelDtoMapper = new CarModelDtoMapper();
        CarModelDto carModelDto1 = carModelDtoMapper.mapEntityToDto(carModel1);
        CarModelDto carModelDto2 = carModelDtoMapper.mapEntityToDto(carModel2);

        CarDtoMapper carDtoMapper = new CarDtoMapper();
        car1 = TestUtil.setEntityAttribute(car1, CarDto::setCarModelDto, carModelDto1, carDtoMapper);
        car2 = TestUtil.setEntityAttribute(car2, CarDto::setCarModelDto, carModelDto2, carDtoMapper);
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        connection.prepareStatement(LOCATION_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.prepareStatement(MODEL_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }

    @Test
    public void findByIdExistingCarReturnsCar() throws Exception {
        long createdId = carDao.create(car1).orElseThrow(AssertionError::new);

        Car foundCar = carDao.findById(createdId).orElseThrow(AssertionError::new);

        assertEquals(foundCar, car1);
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

    @Test
    public void creatingCarWithSameRegistrationPlateReturnsEmptyOptional() throws Exception {
        carDao.create(car1);

        boolean creatingDuplicateEntryResult = carDao.create(car1).isPresent();

        assertFalse(creatingDuplicateEntryResult);
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
    public void updateExistingCarUpdatesCarInDatabase() throws Exception {
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

        boolean addResult = carDao.moveCarToLocation(createdCar.getId(), locationId);

        assertTrue(addResult);
    }

    @Test
    public void addExistingCarToExistingLocationAddsCarToLocation() throws Exception {
        Car createdCar1 = TestUtil.createEntityWithId(car1, carDao);
        Car createdCar2 = TestUtil.createEntityWithId(car2, carDao);

        carDao.moveCarToLocation(createdCar1.getId(), locationId);
        carDao.moveCarToLocation(createdCar2.getId(), locationId);

        List<Car> carsAtLocation = carDao.findCarsAtLocation(locationId);

        assertThat(carsAtLocation, hasItems(car1, car2));
    }

    @Test
    public void addNonExistingCarToExistingLocationReturnsFalse() throws Exception {
        boolean addResult = carDao.moveCarToLocation(-1, locationId);

        assertFalse(addResult);
    }

    @Test(expected = DaoException.class)
    public void addExistingCarToNonExistingLocationShouldThrowException() throws Exception {
        Car createdCar = TestUtil.createEntityWithId(car1, carDao);

        carDao.moveCarToLocation(createdCar.getId(), -1);
    }

    @Test
    public void getCarsAtExistingLocationWithNoCarsReturnsEmptyList() throws Exception {
        List<Car> cars = carDao.findCarsAtLocation(locationId);

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

        long targetModel = car1.getCarModel().getId();
        List<Car> matchingCars = carDao.findCarsByModel(targetModel);

        assertThat(matchingCars, allOf(
                hasItem(car1),
                not(hasItem(car2))));
    }

    @Test
    public void findCarsByNonExistingModelReturnsEmptyList() throws Exception {
        car1 = TestUtil.createEntityWithId(car1, carDao);
        car2 = TestUtil.createEntityWithId(car2, carDao);

        List<Car> matchingCars = carDao.findCarsByModel(-1);

        assertThat(matchingCars, empty());
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
}