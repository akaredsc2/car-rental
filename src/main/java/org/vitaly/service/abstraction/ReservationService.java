package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;

/**
 * Reservation service
 */
public interface ReservationService {
    /**
     * Create new reservation
     *
     * @param reservationDto reservation dto
     * @return true if created, false otherwise
     */
    boolean createNewReservation(ReservationDto reservationDto);

    /**
     * Find reservations of client
     *
     * @param clientDto client dto
     * @return list of client reservation dtos
     */
    List<ReservationDto> findReservationsOfClient(UserDto clientDto);

    /**
     * Find reservations assigned to admin
     *
     * @param adminDto admin dto
     * @return list of admin reservations
     */
    List<ReservationDto> findReservationsAssignedToAdmin(UserDto adminDto);

    /**
     * Find reservations without admin
     *
     * @return list of reservations without admin
     */
    List<ReservationDto> findReservationsWithoutAdmin();

    /**
     * Change reservation state
     *
     * @param reservationDto   reservation dto
     * @param reservationState reservation state
     * @return true if changed, false otherwise
     */
    boolean changeReservationState(ReservationDto reservationDto, String reservationState);

    /**
     * Cancel reservation
     *
     * @param reservationDto reservation dto
     * @return true if canceled, false otherwise
     */
    boolean cancelReservation(ReservationDto reservationDto);

    /**
     * Assign reservation to admin
     *
     * @param reservationDto reservation
     * @param adminDto       admin
     * @return true if assigned, false otherwise
     */
    boolean assignReservationToAdmin(ReservationDto reservationDto, UserDto adminDto);
}
