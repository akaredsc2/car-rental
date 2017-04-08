package org.vitaly.dao.abstraction;

import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;

import java.util.List;

/**
 * Created by vitaly on 2017-04-08.
 */
public interface ReservationDao extends AbstractDao<Reservation> {
    List<Reservation> findReservationsByClientId(long userId);

    List<Reservation> findReservationsByAdminId(long adminId);

    boolean addAdminToReservation(long reservationId, long adminId);

    boolean changeReservationState(long reservationId, ReservationState state);

    boolean addRejectionReason(long reservationId, String rejectionReason);
}
