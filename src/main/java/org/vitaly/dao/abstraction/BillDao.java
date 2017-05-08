package org.vitaly.dao.abstraction;

import org.vitaly.model.bill.Bill;

import java.util.List;

/**
 * Created by vitaly on 2017-04-08.
 */
public interface BillDao extends AbstractDao<Bill> {
    List<Bill> findBillsForReservation(long reservationId);

    boolean addBillToReservation(long billId, long reservationId);

    boolean markPaid(long billId);

    boolean markConfirmed(long billId);
}
