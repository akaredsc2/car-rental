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
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
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
@PrepareForTest({MysqlDaoFactory.class, TransactionManager.class})
@PowerMockIgnore("javax.management.*")
public class BillServiceImplTest {
    private TransactionManager transactionManager = mock(TransactionManager.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private BillDao billDao = mock(BillDao.class);
    private BillService billService = new BillServiceImpl();

    @Test
    public void createNewBill() throws Exception {
        BillDto billDto = new BillDto();
        billDto.setCashAmount(BigDecimal.valueOf(1000));
        billDto.setCreationDateTime(LocalDateTime.now());
        billDto.setDescription("description");

        stab();
        billService.createNewBill(billDto);

        InOrder inOrder = Mockito.inOrder(billDao, transactionManager);
        inOrder.verify(billDao).create(any());
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }

    private void stab() {
        PowerMockito.mockStatic(TransactionManager.class);
//        PowerMockito.when(TransactionManager.startTransaction()).thenReturn(transactionManager);
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
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

        InOrder inOrder = Mockito.inOrder(billDao, transactionManager);
        inOrder.verify(billDao).markAsPaid(billDto.getId());
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }
}
