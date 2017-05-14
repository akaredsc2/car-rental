package org.vitaly.dao.abstraction;

import org.vitaly.model.bill.Bill;

import java.util.List;

/**
 * Created by vitaly on 2017-04-08.
 */

/**
 * Dao for bills
 */
public interface BillDao extends AbstractDao<Bill> {
    /**
     * Find bills for reservation
     *
     * @param reservationId reservation id
     * @return list of bills for reservation
     */
    List<Bill> findBillsForReservation(long reservationId);

    /**
     * Adds bill to reservation
     *
     * @param billId        id of bill
     * @param reservationId id of reservation
     * @return true if bill added, false otherwise
     */
    boolean addBillToReservation(long billId, long reservationId);

    /**
     * Marks bill as paid
     *
     * @param billId id of bill
     * @return true if bill marked, false otherwise
     */
    boolean markPaid(long billId);

    /**
     * Marks bill as confirmed
     *
     * @param billId id of bill
     * @return true if bill confirmed, false otherwise
     */
    boolean markConfirmed(long billId);
}
