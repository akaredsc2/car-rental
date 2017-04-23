package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.dtoMapper.ReservationDtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class ReservationServiceImpl implements ReservationService {
    private TransactionFactory transactionFactory;
    private DtoMapperFactory dtoMapperFactory;

    ReservationServiceImpl(TransactionFactory transactionFactory) {
        this(transactionFactory, new DtoMapperFactory());
    }

    public ReservationServiceImpl(TransactionFactory transactionFactory, DtoMapperFactory dtoMapperFactory) {
        this.transactionFactory = transactionFactory;
        this.dtoMapperFactory = dtoMapperFactory;
    }

    @Override
    public void createNewReservation(ReservationDto reservationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Reservation reservation = dtoMapperFactory.getReservationDtoMapper().mapDtoToEntity(reservationDto);

            ReservationDao reservationDao = transaction.getReservationDao();
            reservationDao.create(reservation);

            transaction.commit();
        }
    }

    @Override
    public List<ReservationDto> getAllMatchingReservations(Predicate<Reservation> predicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Reservation, ReservationDto> mapper = dtoMapperFactory.getReservationDtoMapper();

            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.getAll()
                    .stream()
                    .filter(predicate)
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ReservationDto> findReservationsOfClient(UserDto clientDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Reservation, ReservationDto> mapper = dtoMapperFactory.getReservationDtoMapper();

            long clientId = clientDto.getId();
            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.findReservationsByClientId(clientId)
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ReservationDto> findReservationsAssignedToAdmin(UserDto adminDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Reservation, ReservationDto> mapper = dtoMapperFactory.getReservationDtoMapper();

            long clientId = adminDto.getId();
            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.findReservationsByAdminId(clientId)
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ReservationDto> findReservationsWithoutAdmin() {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Reservation, ReservationDto> mapper = dtoMapperFactory.getReservationDtoMapper();

            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.findReservationsWithoutAdmin()
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void changeReservationState(ReservationDto reservationDto, ReservationState reservationState) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long reservationId = reservationDto.getId();
            ReservationDao reservationDao = transaction.getReservationDao();
            reservationDao.changeReservationState(reservationId, reservationState);

            transaction.commit();
        }
    }

    @Override
    public void assignReservationToAdmin(ReservationDto reservationDto, UserDto adminDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long reservationId = reservationDto.getId();
            long adminId = adminDto.getId();
            ReservationDao reservationDao = transaction.getReservationDao();
            reservationDao.addAdminToReservation(reservationId, adminId);

            transaction.commit();
        }
    }

    @Override
    public void addRejectionReasonToReservation(ReservationDto reservationDto, String reason) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long reservationId = reservationDto.getId();
            ReservationDao reservationDao = transaction.getReservationDao();
            reservationDao.addRejectionReason(reservationId, reason);

            transaction.commit();
        }
    }
}
