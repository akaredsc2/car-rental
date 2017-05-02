package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class AvailableState extends CarState {
    AvailableState() {
    }

    @Override
    boolean makeUnavailable(Car car) {
        car.setState(CarStateEnum.UNAVAILABLE.getState());
        return true;
    }

    @Override
    boolean reserve(Car car) {
        car.setState(CarStateEnum.RESERVED.getState());
        return true;
    }

    @Override
    public String toString() {
        return "available";
    }
}
