package org.vitaly.dao.exception;

/**
 * Exception that happens on dao layer
 */
public class DaoException extends RuntimeException {
    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
