package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.bill.BillDescriptionEnum;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.service.abstraction.BillService;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

/**
 * Created by vitaly on 2017-04-10.
 */
public class BillServiceImpl implements BillService {

    /**
     * @inheritDoc
     */
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
                    .map(this::calculateCashAmount)
                    .map(BigDecimal::new)
                    .map(days -> days.multiply(pricePerDay))
                    .map(Bill::forService);
        }

        return Optional.empty();
    }

    private long calculateCashAmount(Reservation res) {
        int hoursInDay = 24;
        int fullDayPriceThreshold = 3;

        LocalDateTime pickUpDatetime = res.getPickUpDatetime();
        LocalDateTime dropOffDatetime = res.getDropOffDatetime();

        return DAYS.between(pickUpDatetime, dropOffDatetime)
                + HOURS.between(pickUpDatetime, dropOffDatetime) % hoursInDay > fullDayPriceThreshold ? 1 : 0;
    }

    /**
     * @inheritDoc
     */
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

        if (commitCondition) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return commitCondition;
    }

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
    @Override
    public boolean markPaid(BillDto billDto) {
        TransactionManager.startTransaction();

        long billId = billDto.getId();
        boolean isMarked = MysqlDaoFactory.getInstance()
                .getBillDao()
                .markPaid(billId);

        if (isMarked) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return isMarked;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean markConfirmed(BillDto billDto) {
        TransactionManager.startTransaction();

        long billId = billDto.getId();
        BillDao billDao = MysqlDaoFactory.getInstance()
                .getBillDao();

        boolean isPaid = billDao.findById(billId)
                .filter(Bill::isPaid)
                .isPresent();

        boolean isConfirmed = billDao.markConfirmed(billId);

        if (isPaid && isConfirmed) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return isConfirmed;
    }
}
