package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class UnavailableState extends CarState {
    @Override
    boolean makeAvailable(Car car) {
        car.setState(CarStateEnum.AVAILABLE.getState());
        return true;
    }

    @Override
    boolean maintain(Car car) {
        car.setState(CarStateEnum.MAINTAINED.getState());
        return true;
    }

    @Override
    boolean canMakeAvailable() {
        return true;
    }

    @Override
    boolean canMaintain() {
        return true;
    }

    @Override
    public String toString() {
        return "unavailable";
    }
}