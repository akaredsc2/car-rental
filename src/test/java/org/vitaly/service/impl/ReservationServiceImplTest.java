package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.Transaction;
import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MysqlDaoFactory.class, Transaction.class})
@PowerMockIgnore("javax.management.*")
public class ReservationServiceImplTest {
    private Transaction transaction = mock(Transaction.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private ReservationDao reservationDao = mock(ReservationDao.class);
    private ReservationService reservationService = new ReservationServiceImpl();

    @Test
    public void createNewReservationForUser() throws Exception {
        UserDto clientDto = DtoMapperFactory.getInstance()
                .getUserDtoMapper()
                .mapEntityToDto(
                        User.createDummyAdminWithId(3464));

        CarDto carDto = DtoMapperFactory.getInstance()
                .getCarDtoMapper()
                .mapEntityToDto(
                        Car.createDummyCarWithId(1));

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        reservationDto.setPickUpDatetime(LocalDateTime.now());
        reservationDto.setDropOffDatetime(LocalDateTime.now().plusDays(1));
        reservationDto.setState(ReservationStateEnum.APPROVED.getState());

        stab();
        when(reservationDao.create(any())).thenReturn(Optional.empty());
        reservationService.createNewReservation(reservationDto);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    private void stab() {
        PowerMockito.mockStatic(Transaction.class);
        PowerMockito.when(Transaction.startTransaction()).thenReturn(transaction);
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(daoFactory.getReservationDao()).thenReturn(reservationDao);
    }

    @Test
    public void getAllMatchingReservations() throws Exception {
        stab();
        reservationService.getAllMatchingReservations(x -> true);

        verify(reservationDao).getAll();
    }

    @Test
    public void findReservationsOfClient() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(3);

        stab();
        reservationService.findReservationsOfClient(userDto);

        verify(reservationDao).findReservationsByClientId(userDto.getId());
    }

    @Test
    public void findReservationsAssignedToAdmin() throws Exception {
        UserDto adminDto = new UserDto();
        adminDto.setId(5);

        stab();
        reservationService.findReservationsAssignedToAdmin(adminDto);

        verify(reservationDao).findReservationsByAdminId(adminDto.getId());
    }

    @Test
    public void findReservationsWithoutAdmin() throws Exception {
        stab();
        reservationService.findReservationsWithoutAdmin();

        verify(reservationDao).findReservationsWithoutAdmin();
    }

    @Test
    public void changeReservationState() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(34);
        ReservationState reservationState = ReservationStateEnum.PICKED.getState();

        stab();
        reservationService.changeReservationState(reservationDto, reservationState);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).changeReservationState(reservationDto.getId(), reservationState);
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void assignReservationToAdmin() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(549);
        UserDto adminDto = new UserDto();
        adminDto.setId(790);

        stab();
        reservationService.assignReservationToAdmin(reservationDto, adminDto);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).addAdminToReservation(reservationDto.getId(), adminDto.getId());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void addRejectionReasonToReservation() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(549);
        String reason = "reason";

        stab();
        reservationService.addRejectionReasonToReservation(reservationDto, reason);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).addRejectionReason(reservationDto.getId(), reason);
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}
