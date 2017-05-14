package org.vitaly.dao.abstraction;

import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;

import java.util.List;

/**
 * Dao for reservation
 */
public interface ReservationDao extends AbstractDao<Reservation> {

    /**
     * Find reservations of client
     *
     * @param userId client id
     * @return list of client reservations
     */
    List<Reservation> findReservationsByClientId(long userId);

    /**
     * Find reservation of admin
     *
     * @param adminId admin id
     * @return list of reservations assigned to admin
     */
    List<Reservation> findReservationsByAdminId(long adminId);

    /**
     * Assign admin to reservation
     *
     * @param reservationId reservation id
     * @param adminId       admin id
     * @return true if assigned, false otherwise
     */
    boolean addAdminToReservation(long reservationId, long adminId);

    /**
     * Changes reservation state
     *
     * @param reservationId reservation id
     * @param state         reservation state
     * @return true if changed, false otherwise
     */
    boolean changeReservationState(long reservationId, ReservationState state);

    /**
     * Add rejection reason for reservation
     *
     * @param reservationId   reservation id
     * @param rejectionReason rejection reason
     * @return true if added, false otherwise
     */
    boolean addRejectionReason(long reservationId, String rejectionReason);

    /**
     * Find all reservation without admin
     *
     * @return list for unassigned reservations
     */
    List<Reservation> findReservationsWithoutAdmin();

    /**
     * Checks if car is part of reservation that is new, approved or active
     *
     * @param carId car id
     * @return if car is part of reservation that is new, approved or active
     */
    boolean isCarPartOfActiveReservations(long carId);

    /**
     * Checks if admin is assigned to reservation
     *
     * @param adminId       admin id
     * @param reservationId reservation id
     * @return true if admin is assigned to reservation, false otherwise
     */
    boolean isAdminAssignedToReservation(long adminId, long reservationId);
}
