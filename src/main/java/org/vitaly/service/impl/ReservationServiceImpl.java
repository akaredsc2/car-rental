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

        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
        boolean carCanBeReserved = carDao
                .findById(carId)
                .filter(Car::reserve)
                .isPresent();

        if (carCanBeReserved) {
            carDao.changeCarState(carId, CarStateEnum.RESERVED.getState());

            Reservation reservation = DtoMapperFactory.getInstance()
                    .getReservationDtoMapper()
                    .mapDtoToEntity(reservationDto);

            boolean isReservationCreated = MysqlDaoFactory.getInstance()
                    .getReservationDao()
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

        // TODO: 2017-05-05 check if admin is assigned to this reservation
        long reservationId = reservationDto.getId();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance()
                .getReservationDao();
        boolean canChangeState = reservationDao.findById(reservationId)
                .filter(reservation -> checkIfAbleToChangeState(reservation, reservationState))
                .isPresent();

        if (canChangeState) {
            ReservationState state = ReservationStateEnum.stateOf(reservationState)
                    .orElse(ReservationStateEnum.NEW.getState());
            boolean isStateChanged = reservationDao
                    .changeReservationState(reservationId, state);

            // TODO: 2017-05-05 rejected case
            if (state == ReservationStateEnum.REJECTED.getState()) {
                String rejectionReason = reservationDto.getRejectionReason();
                reservationDao.addRejectionReason(reservationId, rejectionReason);
            }

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

        // TODO: 2017-05-05 check reservation state and if claimed by other admin
        long reservationId = reservationDto.getId();
        long adminId = adminDto.getId();

        boolean isAdminAssigned = MysqlDaoFactory.getInstance()
                .getReservationDao()
                .addAdminToReservation(reservationId, adminId);

        TransactionManager.commit();

        return isAdminAssigned;
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
