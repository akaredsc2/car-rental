package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.bill.BillDescriptionEnum;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.service.abstraction.BillService;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by vitaly on 2017-04-10.
 */
public class BillServiceImpl implements BillService {

    @Override
    public Optional<Bill> generateServiceBillForReservation(ReservationDto reservationDto) {
        long reservationId = reservationDto.getId();

        boolean hasNoGeneratedBills = findBillsForReservation(reservationDto).isEmpty();

        if (hasNoGeneratedBills) {
            MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

            BigDecimal pricePerDay = daoFactory
                    .getCarDao()
                    .findCarByReservation(reservationId)
                    .map(Car::getPricePerDay)
                    .orElse(BigDecimal.ZERO);

            return daoFactory
                    .getReservationDao()
                    .findById(reservationId)
                    .map(res -> DAYS.between(res.getPickUpDatetime(), res.getDropOffDatetime()))
                    .map(BigDecimal::new)
                    .map(days -> days.multiply(pricePerDay))
                    .map(Bill::forService);
        }

        return Optional.empty();
    }

    @Override
    public boolean addDamageBillToReservation(BillDto billDto, ReservationDto reservationDto) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_SERIALIZABLE);

        MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

        long reservationId = reservationDto.getId();

        boolean isCarReturned = daoFactory
                .getCarDao()
                .findCarByReservation(reservationId)
                .map(Car::getState)
                .filter(state -> state.equals(CarStateEnum.RETURNED.getState()))
                .isPresent();

        BigDecimal cashAmount = billDto.getCashAmount();
        Bill bill = Bill.forDamage(cashAmount);
        BillDao billDao = daoFactory.getBillDao();

        boolean isOtherBillForDamagePresent = billDao.findBillsForReservation(reservationId)
                .stream()
                .anyMatch(b -> b.getDescription() == BillDescriptionEnum.DAMAGE);

        boolean isBillCreated = billDao
                .create(bill)
                .filter(billId -> billDao.addBillToReservation(billId, reservationId))
                .isPresent();

        boolean commitCondition = isCarReturned && !isOtherBillForDamagePresent && isBillCreated;

        return TransactionManager.endTransaction(commitCondition);
    }

    @Override
    public List<BillDto> findBillsForReservation(ReservationDto reservationDto) {
        DtoMapper<Bill, BillDto> mapper = DtoMapperFactory.getInstance().getBillDtoMapper();

        long reservationId = reservationDto.getId();
        return MysqlDaoFactory.getInstance()
                .getBillDao()
                .findBillsForReservation(reservationId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean markPaid(BillDto billDto) {
        TransactionManager.startTransaction();

        long billId = billDto.getId();
        boolean isMarked = MysqlDaoFactory.getInstance()
                .getBillDao()
                .markPaid(billId);

        return TransactionManager.endTransaction(isMarked);
    }

    @Override
    public boolean markConfirmed(BillDto billDto) {
        TransactionManager.startTransaction();

        long billId = billDto.getId();
        boolean isConfirmed = MysqlDaoFactory.getInstance()
                .getBillDao()
                .markConfirmed(billId);

        return TransactionManager.endTransaction(isConfirmed);
    }
}
