package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.bill.Bill;
import org.vitaly.service.abstraction.BillService;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class BillServiceImpl implements BillService {
    private TransactionFactory transactionFactory;

    public BillServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void createNewBill(BillDto billDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Bill bill = new Bill.Builder()
                    .setDescription(billDto.getDescription())
                    .setCashAmount(billDto.getCashAmount())
                    .setCreationDateTime(LocalDateTime.now())
                    .build();

            BillDao billDao = transaction.getBillDao();
            billDao.create(bill);

            transaction.commit();
        }
    }

    @Override
    public List<Bill> getAllMatchingBills(Predicate<Bill> predicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            BillDao billDao = transaction.getBillDao();
            return billDao.getAll()
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Bill> findBillsForReservation(ReservationDto reservationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long reservationId = reservationDto.getId();
            BillDao billDao = transaction.getBillDao();
            return billDao.findBillsForReservation(reservationId);
        }
    }

    @Override
    public void markAsPaid(BillDto billDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long billId = billDto.getId();
            BillDao billDao = transaction.getBillDao();
            billDao.markAsPaid(billId);

            transaction.commit();
        }
    }
}
