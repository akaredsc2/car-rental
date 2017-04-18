package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.BillService;

/**
 * Created by vitaly on 2017-04-10.
 */
public class BillServiceImpl implements BillService {
    private TransactionFactory transactionFactory;

    public BillServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
