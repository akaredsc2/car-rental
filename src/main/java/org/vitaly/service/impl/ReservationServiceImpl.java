package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.ReservationService;

/**
 * Created by vitaly on 2017-04-10.
 */
public class ReservationServiceImpl implements ReservationService {
    private TransactionFactory transactionFactory;

    public ReservationServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
