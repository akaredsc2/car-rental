package org.vitaly.service.abstraction;

import org.vitaly.model.bill.Bill;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface BillService {
    Optional<Bill> generateServiceBillForReservation(ReservationDto reservationDto);

    boolean addDamageBillToReservation(BillDto billDto, ReservationDto reservationDto);

    List<BillDto> findBillsForReservation(ReservationDto reservationDto);

    boolean markPaid(BillDto billDto);

    boolean markConfirmed(BillDto billDto);
}
