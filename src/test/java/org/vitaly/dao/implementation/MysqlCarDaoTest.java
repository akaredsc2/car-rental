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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by vitaly on 2017-03-27.
 */
public class MysqlCarDaoTest {
    private static final String CAR_CLEAN_UP_QUERY = "delete from car";
    private static final String LOCATION_CLEAN_UP_QUERY = "delete from location";
    private static MysqlConnectionPool pool;
    private static DaoFactory factory;

    private Location location1;
    private Car car1;
    private Car car2;
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
        location1 = new Location.Builder()
                .setState("Kiev region")
                .setCity("Kotsjubinske")
                .setStreet("Ponomarjova")
                .setBuilding("18-a")
                .setCars(new ArrayList<>())
                .build();

        car1 = new Car.Builder()
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setModel("Ford Focus")
                .setPhotoUrl("http://bit.ly/2o8TCb9")
                .setColor("grey")
                .setPricePerDay(BigDecimal.valueOf(100.0))
                .setLocation(location1)
                .build();

        car2 = new Car.Builder()
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setModel("Ford Fiesta")
                .setPhotoUrl("http://bit.ly/2mHkMc3")
                .setColor("blue")
                .setPricePerDay(BigDecimal.valueOf(120.0))
                .setLocation(location1)
                .build();

        connection = pool.getConnection();
        carDao = factory.createCarDao(connection);
        locationDao = factory.createLocationDao(connection);

        Long locationId = locationDao.create(location1).orElseThrow(AssertionError::new);
        Field locationIdField = location1.getClass().getDeclaredField("id");
        locationIdField.setAccessible(true);
        locationIdField.set(location1, locationId);
    }

    @Test
    public void findByIdExistingEntityReturnsEntity() throws Exception {
        Long createdId = carDao.create(car1).orElseThrow(AssertionError::new);

        Car foundCar = carDao.findById(createdId).orElseThrow(AssertionError::new);

        assertThat(foundCar, equalTo(car1));
    }

    @Test
    public void findByIdNonExistingEntityReturnsEmptyOptional() throws Exception {
        boolean findResult = carDao.findById(-1L).isPresent();

        assertThat(findResult, equalTo(false));
    }

    @Test
    public void getAll() throws Exception {

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

    @Test
    public void update() throws Exception {

    }

    @Test
    public void getCarsAtLocation() throws Exception {

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