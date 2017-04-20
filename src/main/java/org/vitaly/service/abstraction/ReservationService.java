package org.vitaly.service.abstraction;

import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface ReservationService {
    void createNewReservation(ReservationDto reservationDto);

    List<Reservation> getAllMatchingReservations(Predicate<Reservation> predicate);

    List<Reservation> findReservationsOfClient(UserDto clientDto);

    List<Reservation> findReservationsAssignedToAdmin(UserDto adminDto);

    List<Reservation> findReservationsWithoutAdmin();

    void changeReservationState(ReservationDto reservationDto, ReservationState reservationState);

    void assignReservationToAdmin(ReservationDto reservationDto, UserDto adminDto);

    void addRejectionReasonToReservation(ReservationDto reservationDto, String reason);
}
