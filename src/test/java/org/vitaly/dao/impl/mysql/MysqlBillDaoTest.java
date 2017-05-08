package org.vitaly.dao.impl.mysql;

import junit.framework.AssertionFailedError;
import org.junit.*;
import org.vitaly.dao.abstraction.*;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.CarDtoMapper;
import org.vitaly.service.impl.dtoMapper.CarModelDtoMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.iterableWithSize;

/**
 * Created by vitaly on 2017-04-08.
 */
public class MysqlBillDaoTest {
    private static final String USERS_CLEAN_UP_QUERY = "delete from users";
    private static final String CAR_CLEAN_UP_QUERY = "delete from car";
    private static final String RESERVATION_CLEAN_UP_QUERY = "delete from reservation";
    private static final String MODEL_CLEAN_UP_QUERY = "delete from model";

    private static Reservation reservation;

    private BillDao billDao = new MysqlBillDao();
    private Bill bill1 = TestData.getInstance().getBill("bill1");
    private Bill bill2 = TestData.getInstance().getBill("bill2");

    @BeforeClass
    public static void init() throws SQLException {
        TransactionManager.startTransaction();

        UserDao userDao = new MysqlUserDao();
        User user = TestUtil.createEntityWithId(TestData.getInstance().getUser("client1"), userDao);

        CarModel carModelFromTestData = TestData.getInstance().getCarModel("carModel1");
        CarModelDao carModelDao = new MysqlCarModelDao();
        CarModel carModel = TestUtil.createEntityWithId(carModelFromTestData, carModelDao);

        CarModelDtoMapper carModelDtoMapper = new CarModelDtoMapper();
        CarModelDto carModelDto = carModelDtoMapper.mapEntityToDto(carModel);

        Car car = TestData.getInstance().getCar("car1");

        CarDtoMapper carDtoMapper = new CarDtoMapper();
        car = TestUtil.setEntityAttribute(car, CarDto::setCarModelDto, carModelDto, carDtoMapper);
        CarDao carDao = new MysqlCarDao();
        car = TestUtil.createEntityWithId(car, carDao);

        Reservation temp = new Reservation.Builder()
                .setClient(user)
                .setCar(car)
                .setPickUpDatetime(LocalDateTime.now())
                .setDropOffDatetime(LocalDateTime.now().plusDays(2))
                .build();
        ReservationDao reservationDao = new MysqlReservationDao();
        reservation = TestUtil.createEntityWithId(temp, reservationDao);

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

        connection.prepareStatement(RESERVATION_CLEAN_UP_QUERY).executeUpdate();
        connection.prepareStatement(USERS_CLEAN_UP_QUERY).executeUpdate();
        connection.prepareStatement(CAR_CLEAN_UP_QUERY).executeUpdate();
        connection.prepareStatement(MODEL_CLEAN_UP_QUERY).executeUpdate();

        TransactionManager.commit();
        connection.close();
    }

    @Test
    public void findByIdExistingBillReturnsBill() throws Exception {
        long createdId = billDao.create(bill1).orElseThrow(AssertionError::new);

        Bill foundBill = billDao.findById(createdId).orElseThrow(AssertionError::new);

        assertEquals(foundBill, bill1);
    }

    @Test
    public void findByIdNOnExistingBillReturnsEmptyOptional() throws Exception {
        boolean isFound = billDao.findById(-1).isPresent();

        assertFalse(isFound);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findIdOfEntityShouldThrowException() throws Exception {
        billDao.findIdOfEntity(bill1);
    }

    @Test
    public void getAllFromEmptyTableReturnsEmptyList() throws Exception {
        List<Bill> billList = billDao.getAll();

        assertThat(billList, empty());
    }

    @Test
    public void getAllReturnsAllBills() throws Exception {
        billDao.create(bill1);
        billDao.create(bill2);

        List<Bill> billList = billDao.getAll();

        assertThat(billList, allOf(
                iterableWithSize(2),
                hasItems(bill1, bill2)));
    }

    @Test
    public void successfulCreateReturnsBillId() throws Exception {
        boolean isCreated = billDao.create(bill1).isPresent();

        assertTrue(isCreated);
    }

    @Test
    public void createdBillIsNotPaid() throws Exception {
        long createdId = billDao.create(bill2).orElseThrow(AssertionError::new);
        Bill createdBill = billDao.findById(createdId).orElseThrow(AssertionError::new);

        assertFalse(createdBill.isPaid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullBillShouldThrowException() throws Exception {
        billDao.create(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateShouldThrowException() throws Exception {
        billDao.update(1, bill1);
    }

    @Test
    public void findBillsForExistingReservationIdReturnsListOfBills() throws Exception {
        long bill1Id = billDao.create(bill1).orElseThrow(AssertionFailedError::new);
        long bill2Id = billDao.create(bill2).orElseThrow(AssertionFailedError::new);
        billDao.addBillToReservation(bill1Id, reservation.getId());
        billDao.addBillToReservation(bill2Id, reservation.getId());

        List<Bill> billsForReservation = billDao.findBillsForReservation(reservation.getId());

        assertThat(billsForReservation, allOf(
                iterableWithSize(2),
                hasItems(bill1, bill2)));
    }

    @Test
    public void findBillsForNonExistingReservationReturnsEmptyList() throws Exception {
        List<Bill> billList = billDao.findBillsForReservation(-1);

        assertThat(billList, empty());
    }

    @Test
    public void addExistingBillToExistingReservationReturnsTrue() throws Exception {
        Bill bill = TestUtil.createEntityWithId(bill1, billDao);

        boolean isAdded = billDao.addBillToReservation(bill.getId(), reservation.getId());

        assertTrue(isAdded);
    }

    @Test(expected = DaoException.class)
    public void addExistingBillToNonExistingReservationShouldThrowException() throws Exception {
        Bill bill = TestUtil.createEntityWithId(bill1, billDao);

        billDao.addBillToReservation(bill.getId(), -1);
    }

    @Test
    public void addNonExistingBillToExistingReservationReturnsFalse() throws Exception {
        boolean isAdded = billDao.addBillToReservation(-1, reservation.getId());

        assertFalse(isAdded);
    }

    @Test
    public void addNonExistingBillToNonExistingReservationReturnsFalse() throws Exception {
        boolean isAdded = billDao.addBillToReservation(-1, -1);

        assertFalse(isAdded);
    }

    @Test
    public void markAsPaidExistingBillReturnsTrue() throws Exception {
        long billId = billDao.create(bill1).orElseThrow(AssertionError::new);

        boolean isMarked = billDao.markPaid(billId);

        assertTrue(isMarked);
    }

    @Test
    public void markAsPaidMarksBillAsPaid() throws Exception {
        long billId = billDao.create(bill1).orElseThrow(AssertionError::new);

        billDao.markPaid(billId);

        Bill paidBill = billDao.findById(billId).orElseThrow(AssertionError::new);

        assertTrue(paidBill.isPaid());
    }

    @Test
    public void markAsPaidNonExistingBillReturnsFalse() throws Exception {
        boolean isMarked = billDao.markPaid(-1);

        assertFalse(isMarked);
    }
}