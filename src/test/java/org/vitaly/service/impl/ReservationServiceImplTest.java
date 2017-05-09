package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;
import org.vitaly.service.abstraction.BillService;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {

    @Mock
    private CarDao carDao;

    @Mock
    private BillDao billDao;

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

    @Mock
    private BillService billService;

    @InjectMocks
    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    private ReservationService service = new ReservationServiceImpl();

    @Test
    public void creatingReservationWithCarThatIsPartOfOtherActiveReservationReturnsFalse() throws Exception {
        UserDto clientDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        Car car = new Car.Builder()
                .setState(CarStateEnum.AVAILABLE.getState())
                .build();

        when(reservationDao.create(any())).thenReturn(Optional.of(10L));
        when(reservationDao.isCarPartOfActiveReservations(anyLong())).thenReturn(true);
        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        boolean isReservationCreated = service.createNewReservation(reservationDto);

        assertFalse(isReservationCreated);
    }

    @Test
    public void creatingReservationWithCarThatCannotBeReservedReturnsFalse() throws Exception {
        UserDto clientDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        Car car = new Car.Builder()
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .build();

        when(reservationDao.create(any())).thenReturn(Optional.of(10L));
        when(reservationDao.isCarPartOfActiveReservations(anyLong())).thenReturn(false);
        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        boolean isReservationCreated = service.createNewReservation(reservationDto);

        assertFalse(isReservationCreated);
    }

    @Test
    public void creatingReservationWithAvailableCarThatIsNotPartOfAnyActiveReservationReturnsTrueAndChangesCarStateToReserved()
            throws Exception {
        UserDto clientDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        Car car = new Car.Builder()
                .setState(CarStateEnum.AVAILABLE.getState())
                .build();

        when(reservationDao.isCarPartOfActiveReservations(anyLong())).thenReturn(false);
        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(reservationDao.create(any())).thenReturn(Optional.of(1L));
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.RESERVED.getState()))).thenReturn(true);
        boolean isReservationCreated = service.createNewReservation(reservationDto);

        verify(carDao).changeCarState(anyLong(), eq(CarStateEnum.RESERVED.getState()));
        assertTrue(isReservationCreated);
    }

    @Test
    public void cancelingReservationOfOtherClientReturnsFalse() throws Exception {
        UserDto clientDto = new UserDto();
        clientDto.setId(10);
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setClient(User.createDummyClientWithId(15))
                .setState(ReservationStateEnum.APPROVED.getState())
                .build();
        Car car = new Car.Builder()
                .setState(CarStateEnum.RESERVED.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.AVAILABLE.getState()))).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        boolean isReservationCanceled = service.cancelReservation(reservationDto);

        assertFalse(isReservationCanceled);
    }

    @Test
    public void cancelingReservationThatIsNotApprovedReturnsFalse() throws Exception {
        long clientId = 10;
        UserDto clientDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setClient(User.createDummyClientWithId(clientId))
                .setState(ReservationStateEnum.NEW.getState())
                .build();
        Car car = new Car.Builder()
                .setState(CarStateEnum.RESERVED.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.AVAILABLE.getState()))).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        boolean isReservationCanceled = service.cancelReservation(reservationDto);

        assertFalse(isReservationCanceled);
    }

    @Test
    public void cancelingReservationSetsCarStateToAvailable() throws Exception {
        long clientId = 10;
        UserDto clientDto = new UserDto();
        clientDto.setId(clientId);
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setClient(User.createDummyClientWithId(clientId))
                .setState(ReservationStateEnum.APPROVED.getState())
                .build();
        Car car = new Car.Builder()
                .setState(CarStateEnum.RESERVED.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.AVAILABLE.getState()))).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), eq(ReservationStateEnum.CANCELED.getState())))
                .thenReturn(true);
        boolean isReservationCanceled = service.cancelReservation(reservationDto);

        verify(carDao).changeCarState(anyLong(), eq(CarStateEnum.AVAILABLE.getState()));
        assertTrue(isReservationCanceled);
    }

    @Test
    public void assigningAdminToNotNewReservationReturnsFalse() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        UserDto adminDto = new UserDto();
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.APPROVED.getState())
                .build();

        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        boolean isReservationAssigned = service.assignReservationToAdmin(reservationDto, adminDto);

        assertFalse(isReservationAssigned);
    }

    @Test
    public void assigningAdminToReservationWithAdminReturnsFalse() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        long adminId = 57;
        UserDto adminDto = new UserDto();
        Reservation reservation = new Reservation.Builder()
                .setAdmin(User.createDummyAdminWithId(adminId))
                .setState(ReservationStateEnum.NEW.getState())
                .build();

        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        boolean isReservationAssigned = service.assignReservationToAdmin(reservationDto, adminDto);

        assertFalse(isReservationAssigned);
    }

    @Test
    public void assigningAdminToNewReservationWithoutAdminReturnsTrue() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        UserDto adminDto = new UserDto();
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.NEW.getState())
                .build();

        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.addAdminToReservation(anyLong(), anyLong())).thenReturn(true);
        boolean isReservationAssigned = service.assignReservationToAdmin(reservationDto, adminDto);

        verify(reservationDao).addAdminToReservation(anyLong(), anyLong());
        assertTrue(isReservationAssigned);
    }

    @Test
    public void changeReservationStateByNotAssignedAdminReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.APPROVED.toString();
        UserDto adminDto = new UserDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.NEW.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(false);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(billDao.create(any())).thenReturn(Optional.of(1L));
        when(billService.generateServiceBillForReservation(any())).thenReturn(Optional.of(new Bill.Builder().build()));
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void changeReservationStateToInappropriateOneReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.APPROVED.toString();
        UserDto adminDto = new UserDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.REJECTED.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(billDao.create(any())).thenReturn(Optional.of(1L));
        when(billService.generateServiceBillForReservation(any())).thenReturn(Optional.of(new Bill.Builder().build()));
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void failingToChangeReservationStateReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.APPROVED.toString();
        UserDto adminDto = new UserDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.NEW.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(false);
        when(billDao.create(any())).thenReturn(Optional.of(1L));
        when(billService.generateServiceBillForReservation(any())).thenReturn(Optional.of(new Bill.Builder().build()));
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void failingToAddRejectionReasonWhenRejectingReservationReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.REJECTED.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        reservationDto.setRejectionReason("Reason");
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.NEW.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(reservationDao.addRejectionReason(anyLong(), anyString())).thenReturn(false);
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.AVAILABLE.getState()))).thenReturn(true);
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void failingToMakeCarAvailableWhenRejectingReservationReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.REJECTED.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        reservationDto.setRejectionReason("Reason");
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.NEW.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(reservationDao.addRejectionReason(anyLong(), anyString())).thenReturn(true);
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.AVAILABLE.getState()))).thenReturn(false);
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void rejectingReservationWithAddingRejectionReasonAndMakingCarAvailableReturnsTrue() throws Exception {
        String targetState = ReservationStateEnum.REJECTED.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        reservationDto.setRejectionReason("Reason");
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.NEW.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(reservationDao.addRejectionReason(anyLong(), anyString())).thenReturn(true);
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.AVAILABLE.getState()))).thenReturn(true);
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertTrue(isStateChanged);
    }

    @Test
    public void failingToServeCarWhenActivatingReservationReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.ACTIVE.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.APPROVED.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.SERVED.getState()))).thenReturn(false);
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void servingCarWhenActivatingReservationReturnsTrue() throws Exception {
        String targetState = ReservationStateEnum.ACTIVE.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.APPROVED.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.SERVED.getState()))).thenReturn(true);
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertTrue(isStateChanged);
    }

    @Test
    public void closingReservationWithNotReturnedCarReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.CLOSED.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.ACTIVE.getState())
                .build();
        Car car = new Car.Builder()
                .setState(CarStateEnum.SERVED.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void failingToMakeCarUnavailableWhileClosingReservationReturnsFalse() throws Exception {
        String targetState = ReservationStateEnum.CLOSED.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.ACTIVE.getState())
                .build();
        Car car = new Car.Builder()
                .setState(CarStateEnum.RETURNED.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.UNAVAILABLE.getState()))).thenReturn(false);
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertFalse(isStateChanged);
    }

    @Test
    public void makingCarUnavailableWhileClosingReservationReturnsTrue() throws Exception {
        String targetState = ReservationStateEnum.CLOSED.toString();
        UserDto adminDto = new UserDto();
        CarDto carDto = new CarDto();
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        Reservation reservation = new Reservation.Builder()
                .setState(ReservationStateEnum.ACTIVE.getState())
                .build();
        Car car = new Car.Builder()
                .setState(CarStateEnum.RETURNED.getState())
                .build();

        when(reservationDao.isAdminAssignedToReservation(anyLong(), anyLong())).thenReturn(true);
        when(reservationDao.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationDao.changeReservationState(anyLong(), any())).thenReturn(true);
        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.changeCarState(anyLong(), eq(CarStateEnum.UNAVAILABLE.getState()))).thenReturn(true);
        boolean isStateChanged = service.changeReservationState(reservationDto, targetState);

        assertTrue(isStateChanged);
    }
}
