package org.vitaly.dao.exception;

/**
 * Created by vitaly on 2017-04-16.
 */
public class DaoException extends RuntimeException {
    public DaoException() {
        super();
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
