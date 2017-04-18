package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.CarService;

/**
 * Created by vitaly on 2017-04-10.
 */
public class CarServiceImpl implements CarService {
    private TransactionFactory transactionFactory;

    public CarServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
