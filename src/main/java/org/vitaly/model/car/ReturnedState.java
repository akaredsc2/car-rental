package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class ReturnedState extends CarState {
    @Override
    boolean makeUnavailable(Car car) {
        car.setState(CarStateEnum.UNAVAILABLE.getState());
        return true;
    }

    @Override
    boolean canMakeUnavailable() {
        return true;
    }
}

