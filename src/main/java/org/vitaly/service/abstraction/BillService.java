package org.vitaly.service.abstraction;

import org.vitaly.model.bill.Bill;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;

import java.util.List;
import java.util.Optional;

/**
 * Bill service
 */
public interface BillService {

    /**
     * Generates service bill for reservation
     *
     * @param reservationDto reservation dto
     * @return bill if created, empty optional otherwise
     */
    Optional<Bill> generateServiceBillForReservation(ReservationDto reservationDto);

    /**
     * Add damage bill to reservation
     *
     * @param billDto        bill dto
     * @param reservationDto reservation dto
     * @return true if added, false otherwise
     */
    boolean addDamageBillToReservation(BillDto billDto, ReservationDto reservationDto);

    /**
     * Find bills for reservation
     *
     * @param reservationDto reservation dto
     * @return list of bill dto associated with reservation
     */
    List<BillDto> findBillsForReservation(ReservationDto reservationDto);

    /**
     * Mark bill as paid
     *
     * @param billDto bill dto
     * @return true if marked, false otherwise
     */
    boolean markPaid(BillDto billDto);

    /**
     * Confirm bill is paid
     *
     * @param billDto bill dto
     * @return true if confirmed, false otherwise
     */
    boolean markConfirmed(BillDto billDto);
}
