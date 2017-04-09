package org.vitaly.dao.implementation;

import org.junit.*;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.time.LocalDateTime;
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

    private static PooledConnection connection;
    private static ReservationDao reservationDao;

    private static User client1;
    private static User client2;
    private static User admin;
    private static Car car1;
    private static Car car2;

    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeClass
    public static void init() {
        MysqlConnectionPool pool = MysqlConnectionPool.getTestInstance();
        connection = pool.getConnection();

        DaoFactory factory = DaoFactory.getMysqlDaoFactory();
        reservationDao = factory.createReservationDao(connection);

        UserDao userDao = factory.createUserDao(connection);
        client1 = TestUtil.createEntityWithId(TestData.getInstance().getUser("client1"), userDao);
        client2 = TestUtil.createEntityWithId(TestData.getInstance().getUser("client2"), userDao);

        admin = TestUtil.createEntityWithId(TestData.getInstance().getUser("admin"), userDao);
        userDao.changeRole(admin.getId(), UserRole.ADMIN);
        admin = userDao.findById(admin.getId()).orElseThrow(AssertionError::new);

        CarDao carDao = factory.createCarDao(connection);
        car1 = TestUtil.createEntityWithId(TestData.getInstance().getCar("car1"), carDao);
        car2 = TestUtil.createEntityWithId(TestData.getInstance().getCar("car2"), carDao);
    }

    @Before
    public void setUp() throws Exception {
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
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(RESERVATION_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        connection.initializeTransaction();
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
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
        reservation2 = TestUtil.createEntityWithId(reservation2, reservationDao);

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
    public void createdReservationHasNewState() throws Exception {
        Reservation reservation = TestUtil.createEntityWithId(reservation1, reservationDao);

        ReservationState state = reservation.getState();

        assertEquals(ReservationStateEnum.NEW.getState(), state);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullReservationShouldThrowException() throws Exception {
        reservationDao.create(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateShouldThrowException() throws Exception {
        reservationDao.update(1, reservation1);
    }

    @Test
    public void findReservationsByExistingClientIdReturnsAllReservationsOfClient() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
        reservation2 = TestUtil.createEntityWithId(reservation2, reservationDao);

        List<Reservation> client1Reservations = reservationDao.findReservationsByClientId(client1.getId());

        assertThat(client1Reservations, allOf(
                iterableWithSize(1),
                hasItems(reservation1)));
    }

    @Test
    public void findReservationsByExistingClientIdWithNoReservationsReturnsEmptyList() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
        reservation2 = TestUtil.createEntityWithId(reservation2, reservationDao);

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
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
        boolean isAdded = reservationDao.addAdminToReservation(reservation1.getId(), admin.getId());

        assertTrue(isAdded);
    }

    @Test
    public void afterAddingAdminReservationContainsAdmin() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
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
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);

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
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);

        boolean isChanged =
                reservationDao.changeReservationState(reservation1.getId(), ReservationStateEnum.APPROVED.getState());

        assertTrue(isChanged);
    }

    @Test
    public void reservationHasChangedStateAfterChangeState() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
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

    @Test(expected = IllegalArgumentException.class)
    public void changeReservationStateToNullShouldThrowException() throws Exception {
        reservationDao.changeReservationState(1, null);
    }

    @Test
    public void addRejectionReasonToExistingReservationReturnsTrue() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);

        boolean isChanged = reservationDao.addRejectionReason(reservation1.getId(), "reason");

        assertTrue(isChanged);
    }

    @Test
    public void reservationHasRejectionReasonAfterAddingRejectionReason() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);

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

    @Test(expected = IllegalArgumentException.class)
    public void addNullRejectionReasonShouldThrowException() throws Exception {
        reservationDao.addRejectionReason(1, null);
    }
}