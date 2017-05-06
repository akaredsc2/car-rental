package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.vitaly.model.reservation.ReservationStateEnum.*;

/**
 * Created by vitaly on 2017-04-10.
 */
public class ReservationServiceImpl implements ReservationService {

    @Override
    public boolean createNewReservation(ReservationDto reservationDto) {
        TransactionManager.startTransaction();

        long carId = reservationDto
                .getCar()
                .getId();

        // TODO: 06.05.17 test
        // TODO: 06.05.17 refactor
        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();

        boolean isCarPartOfActiveReservations = reservationDao
                .isCarPartOfActiveReservations(carId);

        boolean carCanBeReserved = carDao
                .findById(carId)
                .filter(Car::reserve)
                .isPresent();

        if (!isCarPartOfActiveReservations && carCanBeReserved) {
            carDao.changeCarState(carId, CarStateEnum.RESERVED.getState());

            Reservation reservation = DtoMapperFactory.getInstance()
                    .getReservationDtoMapper()
                    .mapDtoToEntity(reservationDto);

            boolean isReservationCreated = reservationDao
                    .create(reservation)
                    .isPresent();

            TransactionManager.commit();
            return isReservationCreated;
        } else {
            TransactionManager.rollback();
            return false;
        }
    }

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

    @Override
    public boolean changeReservationState(ReservationDto reservationDto, String reservationState) {
        TransactionManager.startTransaction();

        long reservationId = reservationDto.getId();
        long adminId = reservationDto.getAdmin().getId();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance()
                .getReservationDao();

        // TODO: 06.05.17 test
        boolean isAdminAssignedToReservation = reservationDao.isAdminAssignedToReservation(adminId, reservationId);
        boolean canChangeState = reservationDao.findById(reservationId)
                .filter(reservation -> checkIfAbleToChangeState(reservation, reservationState))
                .isPresent();

        System.out.println(isAdminAssignedToReservation);
        System.out.println(canChangeState);

        // TODO: 06.05.17 test
        // TODO: 06.05.17 refactor
        if (isAdminAssignedToReservation && canChangeState) {
            ReservationState state = ReservationStateEnum.stateOf(reservationState)
                    .orElse(ReservationStateEnum.NEW.getState());
            boolean isStateChanged = reservationDao
                    .changeReservationState(reservationId, state);

            if (state == ReservationStateEnum.REJECTED.getState()) {
                String rejectionReason = reservationDto.getRejectionReason();
                reservationDao.addRejectionReason(reservationId, rejectionReason);

                long carId = reservationDto.getCar().getId();
                MysqlDaoFactory.getInstance()
                        .getCarDao()
                        .changeCarState(carId, CarStateEnum.AVAILABLE.getState());
            }
//            else if (state == ReservationStateEnum.APPROVED.getState()){
                // TODO: 06.05.17 generate bill
//            }

            TransactionManager.commit();
            return isStateChanged;
        } else {

            TransactionManager.rollback();
            return false;
        }
    }

    @Override
    public boolean assignReservationToAdmin(ReservationDto reservationDto, UserDto adminDto) {
        TransactionManager.startTransaction();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();

        // TODO: 06.05.17 test
        long reservationId = reservationDto.getId();
        boolean isNotClaimedByOtherAdmin = reservationDao
                .findById(reservationId)
                .filter(res -> res.getState() == ReservationStateEnum.NEW.getState() && Objects.isNull(res.getAdmin()))
                .isPresent();

        if (isNotClaimedByOtherAdmin) {
            long adminId = adminDto.getId();
            boolean isAdminAssigned = reservationDao.addAdminToReservation(reservationId, adminId);

            TransactionManager.commit();
            return isAdminAssigned;
        } else {
            TransactionManager.rollback();
            return false;
        }
    }

    private boolean checkIfAbleToChangeState(Reservation reservation, String reservationState) {
        if (reservationState.equalsIgnoreCase(APPROVED.getState().toString())) {
            return reservation.approve();
        }
        if (reservationState.equalsIgnoreCase(REJECTED.getState().toString())) {
            return reservation.reject();
        }
        if (reservationState.equalsIgnoreCase(CANCELED.getState().toString())) {
            return reservation.cancel();
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
