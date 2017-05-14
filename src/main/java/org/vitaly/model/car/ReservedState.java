package org.vitaly.model.car;

/**
 * Reserved car state
 */
public class ReservedState extends CarState {
    ReservedState() {
    }

    /**
     * @inheritDoc
     */
    @Override
    boolean makeAvailable(Car car) {
        car.setState(CarStateEnum.AVAILABLE.getState());
        return true;
    }

    /**
     * @inheritDoc
     */
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
