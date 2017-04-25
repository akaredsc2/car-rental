package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.service.abstraction.BillService;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MysqlDaoFactory.class)
@PowerMockIgnore("javax.management.*")
public class BillServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private BillDao billDao = mock(BillDao.class);
    private BillService billService = new BillServiceImpl(transactionFactory);

    @Test
    public void createNewBill() throws Exception {
        BillDto billDto = new BillDto();
        billDto.setCashAmount(BigDecimal.valueOf(1000));
        billDto.setCreationDateTime(LocalDateTime.now());
        billDto.setDescription("description");

        stab();
        billService.createNewBill(billDto);

        InOrder inOrder = Mockito.inOrder(billDao, transaction);
        inOrder.verify(billDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    private void stab() {
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(daoFactory.getBillDao()).thenReturn(billDao);
    }

    @Test
    public void getAllMatchingBills() throws Exception {
        stab();
        billService.getAllMatchingBills(x -> true);

        verify(billDao).getAll();
    }

    @Test
    public void findBillsForReservation() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(237);

        stab();
        billService.findBillsForReservation(reservationDto);

        verify(billDao).findBillsForReservation(reservationDto.getId());
    }

    @Test
    public void markAsPaid() throws Exception {
        BillDto billDto = new BillDto();
        billDto.setId(189);

        stab();
        billService.markAsPaid(billDto);

        InOrder inOrder = Mockito.inOrder(billDao, transaction);
        inOrder.verify(billDao).markAsPaid(billDto.getId());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}
