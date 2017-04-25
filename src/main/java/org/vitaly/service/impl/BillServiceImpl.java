package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.model.bill.Bill;
import org.vitaly.service.abstraction.BillService;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class BillServiceImpl implements BillService {
    private TransactionFactory transactionFactory;
    private DtoMapperFactory dtoMapperFactory;

    BillServiceImpl(TransactionFactory transactionFactory) {
        this(transactionFactory, new DtoMapperFactory());
    }

    public BillServiceImpl(TransactionFactory transactionFactory, DtoMapperFactory dtoMapperFactory) {
        this.transactionFactory = transactionFactory;
        this.dtoMapperFactory = dtoMapperFactory;
    }

    @Override
    public void createNewBill(BillDto billDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Bill bill = dtoMapperFactory.getBillDtoMapper().mapDtoToEntity(billDto);

            BillDao billDao = MysqlDaoFactory.getInstance().getBillDao();
            billDao.create(bill);

            transaction.commit();
        }
    }

    @Override
    public List<BillDto> getAllMatchingBills(Predicate<Bill> predicate) {
        DtoMapper<Bill, BillDto> mapper = dtoMapperFactory.getBillDtoMapper();

        BillDao billDao = MysqlDaoFactory.getInstance().getBillDao();
        return billDao.getAll()
                .stream()
                .filter(predicate)
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillDto> findBillsForReservation(ReservationDto reservationDto) {
        DtoMapper<Bill, BillDto> mapper = dtoMapperFactory.getBillDtoMapper();

        long reservationId = reservationDto.getId();
        BillDao billDao = MysqlDaoFactory.getInstance().getBillDao();
        return billDao.findBillsForReservation(reservationId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsPaid(BillDto billDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long billId = billDto.getId();
            BillDao billDao = MysqlDaoFactory.getInstance().getBillDao();
            billDao.markAsPaid(billId);

            transaction.commit();
        }
    }
}
