package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.UserService;

/**
 * Created by vitaly on 2017-04-10.
 */
public class UserServiceImpl implements UserService {
    private TransactionFactory transactionFactory;

    public UserServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
