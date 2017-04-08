package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.iterableWithSize;

/**
 * Created by vitaly on 2017-04-08.
 */
public class MysqlReservationDaoTest {
    private static final String USERS_CLEAN_UP_QUERY = "delete from users";
    private static final String CAR_CLEAN_UP_QUERY = "delete from car";
    private static final String RESERVATION_CLEAN_UP_QUERY = "delete from reservation";
    private static MysqlConnectionPool pool;
    private static DaoFactory factory;

    private User client1;
    private User client2;
    private User admin;
    private Car car1;
    private Car car2;
    private Reservation reservation1;
    private Reservation reservation2;

    private PooledConnection connection;
    private ReservationDao reservationDao;
    private UserDao userDao;
    private CarDao carDao;

    @BeforeClass
    public static void initPoolAndFactory() {
        pool = MysqlConnectionPool.getTestInstance();
        factory = DaoFactory.getMysqlDaoFactory();
    }

    @Before
    public void setUp() throws Exception {
        client1 = new User.Builder()
                .setLogin("vitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("ab123456")
                .setDriverLicenceNumber("123sdf456")
                .setRole(UserRole.CLIENT)
                .build();

        client2 = new User.Builder()
                .setLogin("evilVitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("666sat666")
                .setDriverLicenceNumber("1313an1313")
                .setRole(UserRole.CLIENT)
                .build();

        admin = new User.Builder()
                .setLogin("Karsa")
                .setPassword("toblakai")
                .setFullName("Karsa Orlong from Uryd Tribe")
                .setBirthDate(LocalDate.of(1937, Month.JANUARY, 1))
                .setPassportNumber("cd789101")
                .setDriverLicenceNumber("789def101")
                .setRole(UserRole.ADMIN)
                .build();

        car1 = new Car.Builder()
                .setState(CarStateEnum.AVAILABLE.getState())
                .setModel("Ford Focus")
                .setRegistrationPlate("666 satan 666")
                .setPhotoUrl("http://bit.ly/2o8TCb9")
                .setColor("grey")
                .setPricePerDay(BigDecimal.valueOf(100.0))
                .build();

        car2 = new Car.Builder()
                .setState(CarStateEnum.AVAILABLE.getState())
                .setModel("Ford Fiesta")
                .setRegistrationPlate("777 lucky 777")
                .setPhotoUrl("http://bit.ly/2mHkMc3")
                .setColor("blue")
                .setPricePerDay(BigDecimal.valueOf(120.0))
                .build();


        connection = pool.getConnection();
        userDao = factory.createUserDao(connection);
        carDao = factory.createCarDao(connection);

        client1 = createUserWithId(client1);
        client2 = createUserWithId(client2);
        admin = createUserWithId(admin);
        userDao.changeRole(admin, UserRole.ADMIN);

        car1 = createCarWithId(car1);
        car2 = createCarWithId(car2);

        reservation1 = new Reservation.Builder()
                .setClient(client1)
                .setCar(car1)
                .setPickUpDatetime(LocalDateTime.now())
                .setDropOffDatetime(LocalDateTime.now().plusDays(2))
                .build();

        reservation2 = new Reservation.Builder()
                .setClient(client2)
                .setCar(car2)
                .setPickUpDatetime(LocalDateTime.now().plusDays(7))
                .setDropOffDatetime(LocalDateTime.now().plusDays(10))
                .build();

        reservationDao = factory.createReservationDao(connection);
    }

    private User createUserWithId(User user) {
        long createUserId = userDao.create(user).orElseThrow(AssertionError::new);
        return userDao.findById(createUserId).orElseThrow(AssertionError::new);
    }

    private Car createCarWithId(Car car) {
        long createCarId = carDao.create(car).orElseThrow(AssertionError::new);
        return carDao.findById(createCarId).orElseThrow(AssertionError::new);
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(RESERVATION_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.prepareStatement(CAR_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.prepareStatement(USERS_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }

    @Test
    public void findByIdExistingReservationReturnsReservation() throws Exception {
        long createdId = reservationDao.create(reservation1).orElseThrow(AssertionError::new);

        boolean findResult = reservationDao.findById(createdId).isPresent();

        assertTrue(findResult);
    }

    @Test
    public void findByIdNonExistingReservationReturnsEmptyOptional() throws Exception {
        boolean findResult = reservationDao.findById(-1).isPresent();

        assertFalse(findResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findIdOfEntityShouldThrowException() throws Exception {
        reservationDao.findIdOfEntity(reservation1);
    }

    @Test
    public void getAllFromEmptyTableReturnsEmptyList() throws Exception {
        List<Reservation> reservationList = reservationDao.getAll();

        assertThat(reservationList, empty());
    }

    @Test
    public void getAllReturnsAllReservations() throws Exception {
        reservation1 = createReservationWithId(reservation1);
        reservation2 = createReservationWithId(reservation2);

        List<Reservation> reservationList = reservationDao.getAll();

        assertThat(reservationList, allOf(
                iterableWithSize(2),
                hasItems(reservation1, reservation2)));
    }

    @Test
    public void successfulCreationReturnsCreatedId() throws Exception {
        boolean isCreated = reservationDao.create(reservation1).isPresent();

        assertTrue(isCreated);
    }

    @Test
    public void failedCreationReturnsEmptyOptional() throws Exception {
        reservationDao.create(reservation1);

        Field clientIdField = client1.getClass().getDeclaredField("id");
        clientIdField.setAccessible(true);
        clientIdField.set(client1, -1);

        boolean isCreated = reservationDao.create(reservation1).isPresent();

        assertFalse(isCreated);
    }

    @Test
    public void createdReservationHasNewState() throws Exception {
        Reservation reservation = createReservationWithId(reservation1);

        ReservationState state = reservation.getState();

        assertEquals(ReservationStateEnum.NEW.getState(), state);
    }

    private Reservation createReservationWithId(Reservation reservation) {
        long createdId = reservationDao.create(reservation).orElseThrow(AssertionError::new);

        return reservationDao.findById(createdId).orElseThrow(AssertionError::new);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateShouldThrowException() throws Exception {
        reservationDao.update(1, reservation1);
    }

    @Test
    public void findReservationsByExistingClientIdReturnsAllReservationsOfClient() throws Exception {
        reservation1 = createReservationWithId(reservation1);
        reservation2 = createReservationWithId(reservation2);

        List<Reservation> client1Reservations = reservationDao.findReservationsByClientId(client1.getId());

        assertThat(client1Reservations, allOf(
                iterableWithSize(1),
                hasItems(reservation1)));
    }

    @Test
    public void findReservationsByExistingClientIdWithNoReservationsReturnsEmptyList() throws Exception {
        reservation1 = createReservationWithId(reservation1);
        reservation2 = createReservationWithId(reservation2);

        List<Reservation> adminReservations = reservationDao.findReservationsByClientId(admin.getId());

        assertThat(adminReservations, empty());
    }

    @Test
    public void findReservationsByNonExistingClientIdReturnsEmptyList() throws Exception {
        List<Reservation> adminReservations = reservationDao.findReservationsByClientId(-1);

        assertThat(adminReservations, empty());
    }

    @Test
    public void findReservationsByExistingAdminIdReturnsAllReservationsAssignedToAdmin() throws Exception {

    }

    @Test
    public void findReservationsByExistingAdminIdWithNoAssignedReservationsReturnsEmptyList() throws Exception {
        List<Reservation> adminReservations = reservationDao.findReservationsByAdminId(admin.getId());

        assertThat(adminReservations, empty());
    }

    @Test
    public void findReservationsByNonExistingAdminIdReturnsEmptyList() throws Exception {
        List<Reservation> adminReservations = reservationDao.findReservationsByAdminId(-1);

        assertThat(adminReservations, empty());
    }

    @Test
    public void addExistingAdminToExistingReservationReturnsTrue() throws Exception {
        reservation1 = createReservationWithId(reservation1);
        boolean isAdded = reservationDao.addAdminToReservation(reservation1.getId(), admin.getId());

        assertTrue(isAdded);
    }

    @Test
    public void afterAddingAdminReservationContainsAdmin() throws Exception {
        reservation1 = createReservationWithId(reservation1);
        reservationDao.addAdminToReservation(reservation1.getId(), admin.getId());

        Reservation updatedReservation = reservationDao.findById(reservation1.getId()).orElseThrow(AssertionError::new);

        assertEquals(updatedReservation.getAdmin().getId(), admin.getId());
    }

    @Test
    public void addExistingAdminToNonExistingReservationReturnsFalse() throws Exception {
        boolean isAdded = reservationDao.addAdminToReservation(-1, admin.getId());

        assertFalse(isAdded);
    }

    @Test
    public void addNonExistingAdminToExistingReservationReturnsFalse() throws Exception {
        reservation1 = createReservationWithId(reservation1);

        boolean isAdded = reservationDao.addAdminToReservation(reservation1.getId(), -1);
        assertFalse(isAdded);
    }

    @Test
    public void addNonExistingAdminToNonExistingReservationReturnsFalse() throws Exception {
        boolean isAdded = reservationDao.addAdminToReservation(-1, -1);
        assertFalse(isAdded);
    }

    @Test
    public void changeExistingReservationStateReturnsTrue() throws Exception {
        reservation1 = createReservationWithId(reservation1);

        boolean isChanged =
                reservationDao.changeReservationState(reservation1.getId(), ReservationStateEnum.APPROVED.getState());

        assertTrue(isChanged);
    }

    @Test
    public void reservationHasChangedStateAfterChangeState() throws Exception {
        reservation1 = createReservationWithId(reservation1);
        ReservationState beforeChangeState = reservation1.getState();

        ReservationState changedState = ReservationStateEnum.APPROVED.getState();
        reservationDao.changeReservationState(reservation1.getId(), changedState);

        reservation1 = reservationDao.findById(reservation1.getId()).orElseThrow(AssertionError::new);

        ReservationState afterChangeState = reservation1.getState();
        assertThat(afterChangeState, allOf(
                equalTo(changedState),
                not(equalTo(beforeChangeState))));
    }

    @Test
    public void changeNonExistingReservationStateReturnsFalse() throws Exception {
        boolean isChanged = reservationDao.changeReservationState(-1, ReservationStateEnum.APPROVED.getState());

        assertFalse(isChanged);
    }

    @Test
    public void addRejectionReasonToExistingReservationReturnsTrue() throws Exception {
        reservation1 = createReservationWithId(reservation1);

        boolean isChanged = reservationDao.addRejectionReason(reservation1.getId(), "reason");

        assertTrue(isChanged);
    }

    @Test
    public void reservationHasRejectionReasonAfterAddingRejectionReason() throws Exception {
        reservation1 = createReservationWithId(reservation1);

        String reason = "reason";
        reservationDao.addRejectionReason(reservation1.getId(), reason);

        Reservation reservationWithRejectionReason =
                reservationDao.findById(reservation1.getId()).orElseThrow(AssertionError::new);

        assertEquals(reason, reservationWithRejectionReason.getRejectionReason());
    }

    @Test
    public void addRejectionReasonToNonExistingReservationReturnsFalse() throws Exception {
        boolean isChanged = reservationDao.addRejectionReason(-1, "reason");

        assertFalse(isChanged);
    }
}