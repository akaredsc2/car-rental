package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class ReturnedState extends CarState {
    ReturnedState() {
    }

    @Override
    boolean makeUnavailable(Car car) {
        car.setState(CarStateEnum.UNAVAILABLE.getState());
        return true;
    }

    @Override
    public String toString() {
        return "returned";
    }
}

