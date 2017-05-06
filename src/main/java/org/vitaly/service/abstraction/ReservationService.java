package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface ReservationService {
    boolean createNewReservation(ReservationDto reservationDto);

    List<ReservationDto> findReservationsOfClient(UserDto clientDto);

    List<ReservationDto> findReservationsAssignedToAdmin(UserDto adminDto);

    List<ReservationDto> findReservationsWithoutAdmin();

    boolean changeReservationState(ReservationDto reservationDto, String reservationState);

    boolean cancelReservation(ReservationDto reservationDto);

    boolean assignReservationToAdmin(ReservationDto reservationDto, UserDto adminDto);
}
