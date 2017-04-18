package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.NotificationService;

/**
 * Created by vitaly on 2017-04-10.
 */
public class NotificationServiceImpl implements NotificationService {
    private TransactionFactory transactionFactory;

    public NotificationServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
