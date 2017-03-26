package org.vitaly.model.car;

/**
 * Created by vitaly on 2017-03-26.
 */
public class ReservedState extends CarState {
    @Override
    boolean serve(Car car) {
        car.setState(CarStateEnum.SERVED.getState());
        return true;
    }

    @Override
    boolean canServe() {
        return true;
    }
}
