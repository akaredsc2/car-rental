package org.vitaly.dao.abstraction.factory;

import org.vitaly.dao.abstraction.transaction.Transaction;

/**
 * Created by vitaly on 2017-04-17.
 */
public interface TransactionFactory {
    Transaction createTransaction();
}
