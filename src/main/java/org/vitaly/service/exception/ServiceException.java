package org.vitaly.service.exception;

/**
 * Created by vitaly on 2017-04-20.
 */
public class ServiceException extends RuntimeException {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }
}
