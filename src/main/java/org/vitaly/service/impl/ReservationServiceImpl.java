package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.user.User;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class ReservationServiceImpl implements ReservationService {
    private TransactionFactory transactionFactory;

    public ReservationServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void createNewReservation(ReservationDto reservationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            UserDto clientDto = reservationDto.getClient();
            long clientId = clientDto.getId();
            User dummyClient = User.createDummyClientWithId(clientId);

            CarDto carDto = reservationDto.getCar();
            long carId = carDto.getId();
            Car dummyCar = Car.createDummyCarWithId(carId);

            Reservation reservation = new Reservation.Builder()
                    .setClient(dummyClient)
                    .setCar(dummyCar)
                    .setPickUpDatetime(reservationDto.getPickUpDatetime())
                    .setDropOffDatetime(reservationDto.getDropOffDatetime())
                    .build();

            ReservationDao reservationDao = transaction.getReservationDao();
            reservationDao.create(reservation);

            transaction.commit();
        }
    }

    @Override
    public List<Reservation> getAllMatchingReservations(Predicate<Reservation> predicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.getAll()
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Reservation> findReservationsOfClient(UserDto clientDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long clientId = clientDto.getId();
            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.findReservationsByClientId(clientId);
        }
    }

    @Override
    public List<Reservation> findReservationsAssignedToAdmin(UserDto adminDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long clientId = adminDto.getId();
            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.findReservationsByAdminId(clientId);
        }
    }

    @Override
    public List<Reservation> findReservationsWithoutAdmin() {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            ReservationDao reservationDao = transaction.getReservationDao();
            return reservationDao.findReservationsWithoutAdmin();
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
