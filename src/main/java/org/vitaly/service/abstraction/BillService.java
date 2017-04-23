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

    List<BillDto> getAllMatchingBills(Predicate<Bill> predicate);

    List<BillDto> findBillsForReservation(ReservationDto reservationDto);

    void markAsPaid(BillDto billDto);
}
