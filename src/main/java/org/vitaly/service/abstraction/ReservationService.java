package org.vitaly.service.abstraction;

import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface ReservationService {
    Optional<Reservation> findById(long id);

    List<Reservation> getAll();

    Optional<Long> create(Reservation entity);

    List<Reservation> findReservationsByClientId(long userId);

    List<Reservation> findReservationsByAdminId(long adminId);

    boolean addAdminToReservation(long reservationId, long adminId);

    boolean changeReservationState(long reservationId, ReservationState state);

    boolean addRejectionReason(long reservationId, String rejectionReason);

    List<Reservation> findReservationsWithoutAdmin();
}
