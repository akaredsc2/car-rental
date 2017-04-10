package org.vitaly.service.abstraction;

import org.vitaly.model.bill.Bill;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface BillService {
    Optional<Bill> findById(long id);

    List<Bill> getAll();

    Optional<Long> create(Bill bill);

    List<Bill> findBillsForReservation(long reservationId);

    boolean addBillToReservation(long billId, long reservationId);

    boolean markAsPaid(long billId);
}
