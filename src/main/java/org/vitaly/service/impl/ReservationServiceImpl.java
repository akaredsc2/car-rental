package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;
import org.vitaly.service.impl.factory.ServiceFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.vitaly.model.reservation.ReservationStateEnum.*;

/**
 * Created by vitaly on 2017-04-10.
 */
public class ReservationServiceImpl implements ReservationService {

    /**
     * @inheritDoc
     */
    @Override
    public boolean createNewReservation(ReservationDto reservationDto) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        long carId = reservationDto.getCar().getId();
        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();

        boolean isCarPartOfActiveReservations = reservationDao.isCarPartOfActiveReservations(carId);
        boolean isReservationCreated = createReservation(reservationDto, reservationDao);
        boolean isCarReserved = tryToChangeCarState(carId, CarStateEnum.RESERVED.getState(), Car::reserve);

        boolean commitCondition = !isCarPartOfActiveReservations
                && isReservationCreated
                && isCarReserved;

        if (commitCondition) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return commitCondition;
    }

    private boolean createReservation(ReservationDto reservationDto, ReservationDao reservationDao) {
        Reservation reservation = DtoMapperFactory.getInstance()
                .getReservationDtoMapper()
                .mapDtoToEntity(reservationDto);

        return reservationDao
                .create(reservation)
                .isPresent();
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<ReservationDto> findReservationsOfClient(UserDto clientDto) {
        DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();

        long clientId = clientDto.getId();
        return MysqlDaoFactory.getInstance()
                .getReservationDao()
                .findReservationsByClientId(clientId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<ReservationDto> findReservationsAssignedToAdmin(UserDto adminDto) {
        DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();

        long clientId = adminDto.getId();
        return MysqlDaoFactory.getInstance()
                .getReservationDao()
                .findReservationsByAdminId(clientId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<ReservationDto> findReservationsWithoutAdmin() {
        DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getReservationDao()
                .findReservationsWithoutAdmin()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean changeReservationState(ReservationDto reservationDto, String reservationState) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        long reservationId = reservationDto.getId();
        long adminId = reservationDto.getAdmin().getId();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();

        boolean isAdminAssignedToReservation = reservationDao.isAdminAssignedToReservation(adminId, reservationId);
        boolean canChangeState = reservationDao.findById(reservationId)
                .filter(reservation -> checkIfAbleToChangeState(reservation, reservationState))
                .isPresent();
        boolean isStateChanged = doChangeState(reservationDto, reservationDao, reservationState);

        boolean commitCondition = isAdminAssignedToReservation && canChangeState && isStateChanged;

        if (commitCondition) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return commitCondition;
    }

    private boolean doChangeState(ReservationDto reservationDto,
                                  ReservationDao reservationDao, String reservationState) {
        ReservationState state = ReservationStateEnum.stateOf(reservationState)
                .orElse(ReservationStateEnum.NEW.getState());

        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();

        boolean isReservationStateChanged = reservationDao.changeReservationState(reservationDto.getId(), state);

        if (state == ReservationStateEnum.REJECTED.getState()) {
            return isReservationStateChanged && isReservationRejected(reservationDto, reservationDao, carDao);
        } else if (state == ReservationStateEnum.APPROVED.getState()) {
            return isReservationStateChanged && isBillAdded(reservationDto);
        } else if (state == ReservationStateEnum.ACTIVE.getState()) {
            return isReservationStateChanged && isReservationActivated(reservationDto);
        } else if (state == ReservationStateEnum.CLOSED.getState()) {
            return isReservationStateChanged && isReservationClosed(reservationDto);
        }

        return false;
    }

    private boolean isBillAdded(ReservationDto reservationDto) {
        BillDao billDao = MysqlDaoFactory.getInstance().getBillDao();

        return ServiceFactory.getInstance()
                .getBillService()
                .generateServiceBillForReservation(reservationDto)
                .map(billDao::create)
                .map(Optional::get)
                .map(billId -> billDao.addBillToReservation(billId, reservationDto.getId()))
                .isPresent();
    }

    private boolean isReservationClosed(ReservationDto reservationDto) {
        long reservationId = reservationDto.getId();
        long carId = reservationDto.getCar().getId();

        boolean isCarReturned = tryToChangeCarState(carId, CarStateEnum.UNAVAILABLE.getState(), Car::makeUnavailable);
        boolean areBillsPaid = areBillsPaid(reservationId);

        return isCarReturned && areBillsPaid;
    }

    private boolean isReservationActivated(ReservationDto reservationDto) {
        long reservationId = reservationDto.getId();
        long carId = reservationDto.getCar().getId();

        boolean isBillPaid = areBillsPaid(reservationId);
        boolean isCarServed = tryToChangeCarState(carId, CarStateEnum.SERVED.getState(), Car::serve);

        return isCarServed && isBillPaid;
    }

    private boolean areBillsPaid(long reservationId) {
        return MysqlDaoFactory.getInstance()
                .getBillDao()
                .findBillsForReservation(reservationId)
                .stream()
                .allMatch(bill -> bill.isPaid() && bill.isConfirmed());
    }

    private boolean isReservationRejected(ReservationDto reservationDto, ReservationDao reservationDao, CarDao carDao) {
        boolean isRejectionReasonAdded = reservationDao
                .addRejectionReason(reservationDto.getId(), reservationDto.getRejectionReason());
        boolean isCarAvailable = carDao
                .changeCarState(reservationDto.getCar().getId(), CarStateEnum.AVAILABLE.getState());
        return isRejectionReasonAdded && isCarAvailable;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean cancelReservation(ReservationDto reservationDto) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        long reservationId = reservationDto.getId();
        long clientId = reservationDto.getClient().getId();
        long carId = reservationDto.getCar().getId();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance()
                .getReservationDao();

        boolean isAbleToCancel = canReservationBeCanceled(reservationId, clientId, reservationDao);
        boolean isReservationCanceled = reservationDao
                .changeReservationState(reservationId, ReservationStateEnum.CANCELED.getState());
        boolean isCarMadeAvailable = tryToChangeCarState(carId, CarStateEnum.AVAILABLE.getState(), Car::makeAvailable);

        boolean commitCondition = isAbleToCancel
                && isReservationCanceled
                && isCarMadeAvailable;

        if (commitCondition) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return commitCondition;
    }

    private boolean tryToChangeCarState(long carId, CarState state, Predicate<Car> stateChangePredicate) {
        CarDao carDao = MysqlDaoFactory.getInstance()
                .getCarDao();

        return carDao.findById(carId)
                .filter(stateChangePredicate)
                .map(Car::getId)
                .filter(id -> carDao.changeCarState(id, state))
                .isPresent();
    }

    private boolean canReservationBeCanceled(long reservationId, long clientId, ReservationDao reservationDao) {
        return reservationDao.findById(reservationId)
                .filter(reservation -> reservation.getClient().getId() == clientId)
                .filter(Reservation::cancel)
                .isPresent();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean assignReservationToAdmin(ReservationDto reservationDto, UserDto adminDto) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        long reservationId = reservationDto.getId();
        long adminId = adminDto.getId();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();

        boolean isNotClaimedByOtherAdmin = reservationDao
                .findById(reservationId)
                .filter(res -> res.getState() == ReservationStateEnum.NEW.getState() && Objects.isNull(res.getAdmin()))
                .isPresent();

        boolean isAdminAssigned = reservationDao.addAdminToReservation(reservationId, adminId);

        boolean commitCondition = isNotClaimedByOtherAdmin && isAdminAssigned;

        if (commitCondition) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return commitCondition;
    }

    private boolean checkIfAbleToChangeState(Reservation reservation, String reservationState) {
        if (reservationState.equalsIgnoreCase(APPROVED.getState().toString())) {
            return reservation.approve();
        }
        if (reservationState.equalsIgnoreCase(REJECTED.getState().toString())) {
            return reservation.reject();
        }
        if (reservationState.equalsIgnoreCase(ACTIVE.getState().toString())) {
            return reservation.activate();
        }
        if (reservationState.equalsIgnoreCase(CLOSED.getState().toString())) {
            return reservation.close();
        }
        return false;
    }
}
