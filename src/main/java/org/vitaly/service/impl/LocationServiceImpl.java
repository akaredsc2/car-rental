package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.LocationService;

/**
 * Created by vitaly on 2017-04-10.
 */
public class LocationServiceImpl implements LocationService {
    private TransactionFactory transactionFactory;

    public LocationServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
