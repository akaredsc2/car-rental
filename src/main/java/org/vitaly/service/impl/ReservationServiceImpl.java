package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class ReservationServiceImpl implements ReservationService {

    @Override
    public void createNewReservation(ReservationDto reservationDto) {
        TransactionManager.startTransaction();

        Reservation reservation = DtoMapperFactory.getInstance()
                .getReservationDtoMapper()
                .mapDtoToEntity(reservationDto);

        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        reservationDao.create(reservation);

        TransactionManager.commit();
    }

    @Override
    public List<ReservationDto> getAllMatchingReservations(Predicate<Reservation> predicate) {
        DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        return reservationDao.getAll()
                .stream()
                .filter(predicate)
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> findReservationsOfClient(UserDto clientDto) {
        DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();

        long clientId = clientDto.getId();
        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        return reservationDao.findReservationsByClientId(clientId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> findReservationsAssignedToAdmin(UserDto adminDto) {
        DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();

        long clientId = adminDto.getId();
        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        return reservationDao.findReservationsByAdminId(clientId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> findReservationsWithoutAdmin() {
        DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();

        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        return reservationDao.findReservationsWithoutAdmin()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changeReservationState(ReservationDto reservationDto, ReservationState reservationState) {
        TransactionManager.startTransaction();

        long reservationId = reservationDto.getId();
        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        reservationDao.changeReservationState(reservationId, reservationState);

        TransactionManager.commit();
    }

    @Override
    public void assignReservationToAdmin(ReservationDto reservationDto, UserDto adminDto) {
        TransactionManager.startTransaction();

        long reservationId = reservationDto.getId();
        long adminId = adminDto.getId();
        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        reservationDao.addAdminToReservation(reservationId, adminId);

        TransactionManager.commit();
    }

    @Override
    public void addRejectionReasonToReservation(ReservationDto reservationDto, String reason) {
        TransactionManager.startTransaction();

        long reservationId = reservationDto.getId();
        ReservationDao reservationDao = MysqlDaoFactory.getInstance().getReservationDao();
        reservationDao.addRejectionReason(reservationId, reason);

        TransactionManager.startTransaction();
    }
}
