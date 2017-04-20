package org.vitaly.service.abstraction;

import org.vitaly.model.bill.Bill;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface BillService {
    void createNewBill(BillDto billDto);

    List<Bill> getAllMatchingBills(Predicate<Bill> predicate);

    List<Bill> findBillsForReservation(ReservationDto reservationDto);

    void markAsPaid(BillDto billDto);
}
