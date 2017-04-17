package org.vitaly.dao.impl.mysql.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.ConnectionPool;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.transaction.MysqlTransaction;

import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-17.
 */
public class MysqlTransactionFactory implements TransactionFactory {
    private static Logger logger = LogManager.getLogger(MysqlTransactionFactory.class.getName());

    private ConnectionPool connectionPool;

    public MysqlTransactionFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Transaction createTransaction() {
        try {
            PooledConnection pooledConnection = connectionPool.getConnection();
            return MysqlTransaction.createTransaction(pooledConnection);
        } catch (SQLException e) {
            String message = "Error while creating transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }
}
