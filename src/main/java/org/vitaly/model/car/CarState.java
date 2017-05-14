package org.vitaly.model.car;

/**
 * State of car. Part of State design pattern
 */
public abstract class CarState {

    /**
     * Make car available
     *
     * @param car context car
     * @return true if made available, false otherwise
     */
    boolean makeAvailable(Car car) {
        return false;
    }

    /**
     * Make car unavailable
     *
     * @param car context car
     * @return true if made unavailable, false otherwise
     */
    boolean makeUnavailable(Car car) {
        return false;
    }

    /**
     * Make car reserved
     *
     * @param car context car
     * @return true if made reserved, false otherwise
     */
    boolean reserve(Car car) {
        return false;
    }

    /**
     * Make car served
     *
     * @param car context car
     * @return true if made served, false otherwise
     */
    boolean serve(Car car) {
        return false;
    }

    /**
     * Make car returned
     *
     * @param car context car
     * @return true if made returned, false otherwise
     */
    boolean doReturn(Car car) {
        return false;
    }
}
