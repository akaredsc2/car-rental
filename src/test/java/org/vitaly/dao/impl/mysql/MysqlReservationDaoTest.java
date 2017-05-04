package org.vitaly.dao.impl.mysql;

import org.junit.*;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.car.Car;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.CarDtoMapper;
import org.vitaly.service.impl.dtoMapper.CarModelDtoMapper;

import java.sql.SQLException;
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
    private static final String MODEL_CLEAN_UP_QUERY = "delete from model";

    private static User client1;
    private static User client2;
    private static User admin;
    private static Car car1;
    private static Car car2;

    private ReservationDao reservationDao = new MysqlReservationDao();
    private Reservation reservation1 = new Reservation.Builder()
            .setClient(client1)
            .setCar(car1)
            .setPickUpDatetime(LocalDateTime.now())
            .setDropOffDatetime(LocalDateTime.now().plusDays(2))
            .build();

    private Reservation reservation2 = new Reservation.Builder()
            .setClient(client2)
            .setCar(car2)
            .setPickUpDatetime(LocalDateTime.now().plusDays(7))
            .setDropOffDatetime(LocalDateTime.now().plusDays(10))
            .build();

    @BeforeClass
    public static void init() throws SQLException {
        TransactionManager.startTransaction();

        UserDao userDao = new MysqlUserDao();
        client1 = TestUtil.createEntityWithId(TestData.getInstance().getUser("client1"), userDao);
        client2 = TestUtil.createEntityWithId(TestData.getInstance().getUser("client2"), userDao);

        admin = TestUtil.createEntityWithId(TestData.getInstance().getUser("admin"), userDao);
        userDao.changeRole(admin.getId(), UserRole.ADMIN);
        admin = userDao.findById(admin.getId()).orElseThrow(AssertionError::new);

        CarModel carModelFromTestData1 = TestData.getInstance().getCarModel("carModel1");
        CarModel carModelFromTestData2 = TestData.getInstance().getCarModel("carModel2");
        CarModelDao carModelDao = new MysqlCarModelDao();
        CarModel carModel1 = TestUtil.createEntityWithId(carModelFromTestData1, carModelDao);
        CarModel carModel2 = TestUtil.createEntityWithId(carModelFromTestData2, carModelDao);

        CarModelDtoMapper carModelDtoMapper = new CarModelDtoMapper();
        CarModelDto carModelDto1 = carModelDtoMapper.mapEntityToDto(carModel1);
        CarModelDto carModelDto2 = carModelDtoMapper.mapEntityToDto(carModel2);

        Car carFromTestData1 = TestData.getInstance().getCar("car1");
        Car carFromTestData2 = TestData.getInstance().getCar("car2");

        CarDtoMapper carDtoMapper = new CarDtoMapper();
        CarDto carDto1 = carDtoMapper.mapEntityToDto(carFromTestData1);
        CarDto carDto2 = carDtoMapper.mapEntityToDto(carFromTestData2);
        carDto1.setCarModelDto(carModelDto1);
        carDto2.setCarModelDto(carModelDto2);
        carFromTestData1 = carDtoMapper.mapDtoToEntity(carDto1);
        carFromTestData2 = carDtoMapper.mapDtoToEntity(carDto2);

        CarDao carDao = new MysqlCarDao();
        MysqlReservationDaoTest.car1 = TestUtil.createEntityWithId(carFromTestData1, carDao);
        MysqlReservationDaoTest.car2 = TestUtil.createEntityWithId(carFromTestData2, carDao);

        TransactionManager.commit();
        TransactionManager.getConnection().close();
    }

    @Before
    public void setUp() throws Exception {
        TransactionManager.startTransaction();
    }

    @After
    public void tearDown() throws Exception {
        TransactionManager.rollback();
        TransactionManager.getConnection().close();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        TransactionManager.startTransaction();

        PooledConnection connection = TransactionManager.getConnection();

        connection.prepareStatement(CAR_CLEAN_UP_QUERY).executeUpdate();
        connection.prepareStatement(MODEL_CLEAN_UP_QUERY).executeUpdate();
        connection.prepareStatement(USERS_CLEAN_UP_QUERY).executeUpdate();

        TransactionManager.commit();
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

    @Test(expected = DaoException.class)
    public void addNonExistingAdminToExistingReservationShouldThrowException() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);

        reservationDao.addAdminToReservation(reservation1.getId(), -1);
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

    @Test
    public void findReservationsWithoutAdminReturnsListOfReservationsWithoutAdmin() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
        reservation2 = TestUtil.createEntityWithId(reservation2, reservationDao);

        reservationDao.addAdminToReservation(reservation1.getId(), admin.getId());

        List<Reservation> reservationsWithoutAdmin = reservationDao.findReservationsWithoutAdmin();

        assertThat(reservationsWithoutAdmin, allOf(
                not(hasItem(reservation1)),
                hasItem(reservation2)));
    }

    @Test
    public void findReservationsWithoutAdminReturnsEmptyListOnEmptyTable() throws Exception {
        List<Reservation> reservationsWithoutAdmin = reservationDao.findReservationsWithoutAdmin();

        assertThat(reservationsWithoutAdmin, empty());
    }

    @Test
    public void findReservationsWithoutAdminEmptyListIfAllReservationsHaveAdmin() throws Exception {
        reservation1 = TestUtil.createEntityWithId(reservation1, reservationDao);
        reservation2 = TestUtil.createEntityWithId(reservation2, reservationDao);

        reservationDao.addAdminToReservation(reservation1.getId(), admin.getId());
        reservationDao.addAdminToReservation(reservation2.getId(), admin.getId());

        List<Reservation> reservationsWithoutAdmin = reservationDao.findReservationsWithoutAdmin();

        assertThat(reservationsWithoutAdmin, empty());
    }
}