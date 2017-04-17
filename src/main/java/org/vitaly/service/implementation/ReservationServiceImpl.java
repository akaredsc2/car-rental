package org.vitaly.service.implementation;

import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
//import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.service.abstraction.ReservationService;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public class ReservationServiceImpl implements ReservationService {
    private ReservationDao dao;

    public ReservationServiceImpl(PooledConnection connection) {
//        this.dao = DaoFactory.getMysqlDaoFactory().createReservationDao(connection);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Reservation> getAll() {
        return dao.getAll();
    }

    @Override
    public Optional<Long> create(Reservation entity) {
        return dao.create(entity);
    }

    @Override
    public List<Reservation> findReservationsByClientId(long userId) {
        return dao.findReservationsByClientId(userId);
    }

    @Override
    public List<Reservation> findReservationsByAdminId(long adminId) {
        return dao.findReservationsByAdminId(adminId);
    }

    @Override
    public boolean addAdminToReservation(long reservationId, long adminId) {
        return dao.addAdminToReservation(reservationId, adminId);
    }

    @Override
    public boolean changeReservationState(long reservationId, ReservationState state) {
        return dao.changeReservationState(reservationId, state);
    }

    @Override
    public boolean addRejectionReason(long reservationId, String rejectionReason) {
        return dao.addRejectionReason(reservationId, rejectionReason);
    }

    @Override
    public List<Reservation> findReservationsWithoutAdmin() {
        return dao.findReservationsWithoutAdmin();
    }
}
