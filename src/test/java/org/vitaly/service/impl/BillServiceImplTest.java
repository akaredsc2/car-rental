package org.vitaly.service.impl;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.service.abstraction.BillService;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-20.
 */
public class BillServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private BillDao billDao = mock(BillDao.class);
    private BillService billService = new BillServiceImpl(transactionFactory);

    @Test
    public void createNewBill() throws Exception {
        BillDto billDto = new BillDto();
        billDto.setCashAmount(BigDecimal.valueOf(1000));
        billDto.setCreationDateTime(LocalDateTime.now());
        billDto.setDescription("description");

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getBillDao()).thenReturn(billDao);
        billService.createNewBill(billDto);

        InOrder inOrder = Mockito.inOrder(billDao, transaction);
        inOrder.verify(billDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void getAllMatchingBills() throws Exception {
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getBillDao()).thenReturn(billDao);
        billService.getAllMatchingBills(x -> true);

        InOrder inOrder = Mockito.inOrder(billDao, transaction);
        inOrder.verify(billDao).getAll();
        inOrder.verify(transaction).close();
    }

    @Test
    public void findBillsForReservation() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(237);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getBillDao()).thenReturn(billDao);
        billService.findBillsForReservation(reservationDto);

        InOrder inOrder = Mockito.inOrder(billDao, transaction);
        inOrder.verify(billDao).findBillsForReservation(reservationDto.getId());
        inOrder.verify(transaction).close();
    }

    @Test
    public void markAsPaid() throws Exception {
        BillDto billDto = new BillDto();
        billDto.setId(189);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getBillDao()).thenReturn(billDao);
        billService.markAsPaid(billDto);

        InOrder inOrder = Mockito.inOrder(billDao, transaction);
        inOrder.verify(billDao).markAsPaid(billDto.getId());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}
