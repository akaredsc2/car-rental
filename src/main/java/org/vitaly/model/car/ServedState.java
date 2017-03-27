package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class ServedState extends CarState {
    @Override
    boolean doReturn(Car car) {
        car.setState(CarStateEnum.RETURNED.getState());
        return true;
    }

    @Override
    boolean canReturn() {
        return true;
    }

    @Override
    public String toString() {
        return "served";
    }
}