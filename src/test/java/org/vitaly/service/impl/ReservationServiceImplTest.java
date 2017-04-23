package org.vitaly.service.impl;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-20.
 */
public class ReservationServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private ReservationDao reservationDao = mock(ReservationDao.class);
    private ReservationService reservationService = new ReservationServiceImpl(transactionFactory);

    @Test
    public void createNewReservationForUser() throws Exception {
        UserDto clientDto = new UserDto();
        clientDto.setId(1);
        CarDto carDto = new CarDto();
        carDto.setId(2);
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClient(clientDto);
        reservationDto.setCar(carDto);
        reservationDto.setPickUpDatetime(LocalDateTime.now());
        reservationDto.setDropOffDatetime(LocalDateTime.now().plusDays(1));
        reservationDto.setState(ReservationStateEnum.APPROVED.getState());

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
        reservationService.createNewReservation(reservationDto);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void getAllMatchingReservations() throws Exception {
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
        reservationService.getAllMatchingReservations(x -> true);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).getAll();
        inOrder.verify(transaction).close();
    }

    @Test
    public void findReservationsOfClient() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(3);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
        reservationService.findReservationsOfClient(userDto);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).findReservationsByClientId(userDto.getId());
        inOrder.verify(transaction).close();
    }

    @Test
    public void findReservationsAssignedToAdmin() throws Exception {
        UserDto adminDto = new UserDto();
        adminDto.setId(5);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
        reservationService.findReservationsAssignedToAdmin(adminDto);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).findReservationsByAdminId(adminDto.getId());
        inOrder.verify(transaction).close();

    }

    @Test
    public void findReservationsWithoutAdmin() throws Exception {
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
        reservationService.findReservationsWithoutAdmin();

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).findReservationsWithoutAdmin();
        inOrder.verify(transaction).close();
    }

    @Test
    public void changeReservationState() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(34);
        ReservationState reservationState = ReservationStateEnum.PICKED.getState();

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
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

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
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

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getReservationDao()).thenReturn(reservationDao);
        reservationService.addRejectionReasonToReservation(reservationDto, reason);

        InOrder inOrder = Mockito.inOrder(reservationDao, transaction);
        inOrder.verify(reservationDao).addRejectionReason(reservationDto.getId(), reason);
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}
