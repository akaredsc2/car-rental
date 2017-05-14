package org.vitaly.model.car;

/**
 * Returned car state
 */
public class ReturnedState extends CarState {
    ReturnedState() {
    }

    /**
     * @inheritDoc
     */
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

