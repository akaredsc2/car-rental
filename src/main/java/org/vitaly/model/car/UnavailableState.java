package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class UnavailableState extends CarState {
    UnavailableState() {
    }

    @Override
    boolean makeAvailable(Car car) {
        car.setState(CarStateEnum.AVAILABLE.getState());
        return true;
    }

    @Override
    public String toString() {
        return "unavailable";
    }
}