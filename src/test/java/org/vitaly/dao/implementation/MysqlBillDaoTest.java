package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.*;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;

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
    private static final String CLEAN_UP_QUERY = "delete from bill";
    private static final String USERS_CLEAN_UP_QUERY = "delete from users";
    private static final String CAR_CLEAN_UP_QUERY = "delete from car";
    private static final String RESERVATION_CLEAN_UP_QUERY = "delete from reservation";

    private static PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();
    private static BillDao billDao;
    private static Reservation reservation;

    private Bill bill1 = TestData.getInstance().getBill("bill1");
    private Bill bill2 = TestData.getInstance().getBill("bill2");

    @BeforeClass
    public static void init() {
        DaoFactory factory = DaoFactory.getMysqlDaoFactory();
        billDao = factory.createBillDao(connection);

        UserDao userDao = factory.createUserDao(connection);
        User user = TestUtil.createEntityWithId(TestData.getInstance().getUser("client1"), userDao);

        CarDao carDao = factory.createCarDao(connection);
        Car car = TestUtil.createEntityWithId(TestData.getInstance().getCar("car1"), carDao);

        Reservation temp = new Reservation.Builder()
                .setClient(user)
                .setCar(car)
                .setPickUpDatetime(LocalDateTime.now())
                .setDropOffDatetime(LocalDateTime.now().plusDays(2))
                .build();
        ReservationDao reservationDao = factory.createReservationDao(connection);
        reservation = TestUtil.createEntityWithId(temp, reservationDao);
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(RESERVATION_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.prepareStatement(USERS_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.prepareStatement(CAR_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
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

        boolean isMarked = billDao.markAsPaid(billId);

        assertTrue(isMarked);
    }

    @Test
    public void markAsPaidMarksBillAsPaid() throws Exception {
        long billId = billDao.create(bill1).orElseThrow(AssertionError::new);

        billDao.markAsPaid(billId);

        Bill paidBill = billDao.findById(billId).orElseThrow(AssertionError::new);

        assertTrue(paidBill.isPaid());
    }

    @Test
    public void markAsPaidNonExistingBillReturnsFalse() throws Exception {
        boolean isMarked = billDao.markAsPaid(-1);

        assertFalse(isMarked);
    }
}