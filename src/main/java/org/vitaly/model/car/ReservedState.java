package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class ReservedState extends CarState {
    ReservedState() {
    }

    @Override
    boolean makeAvailable(Car car) {
        car.setState(CarStateEnum.AVAILABLE.getState());
        return true;
    }

    @Override
    boolean serve(Car car) {
        car.setState(CarStateEnum.SERVED.getState());
        return true;
    }

    @Override
    public String toString() {
        return "reserved";
    }
}
