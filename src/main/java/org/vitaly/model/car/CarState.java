package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public abstract class CarState {
    boolean makeAvailable(Car car) {
        return false;
    }

    boolean makeUnavailable(Car car) {
        return false;
    }

    boolean reserve(Car car) {
        return false;
    }

    boolean serve(Car car) {
        return false;
    }

    boolean doReturn(Car car) {
        return false;
    }
}
