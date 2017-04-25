package org.vitaly.model.car;

import org.vitaly.model.Entity;
import org.vitaly.model.carModel.CarModel;

import java.math.BigDecimal;

/**
 * Created by vitaly on 2017-03-26.
 */
public class Car implements Entity {
    private long id;
    private CarState state;
    private CarModel carModel;
    private String registrationPlate;
    private String color;
    private BigDecimal pricePerDay;

    private Car(Builder builder) {
        this.id = builder.id;
        this.state = builder.state;
        this.carModel = builder.carModel;
        this.registrationPlate = builder.registrationPlate;
        this.color = builder.color;
        this.pricePerDay = builder.pricePerDay;
    }

    @Override
    public long getId() {
        return id;
    }

    public CarState getState() {
        return state;
    }

    void setState(CarState state) {
        this.state = state;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public String getColor() {
        return color;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public boolean makeAvailable() {
        return state.makeAvailable(this);
    }

    public boolean makeUnavailable() {
        return state.makeUnavailable(this);
    }

    public boolean maintain() {
        return state.maintain(this);
    }

    public boolean reserve() {
        return state.reserve(this);
    }

    public boolean serve() {
        return state.serve(this);
    }

    public boolean doReturn() {
        return state.doReturn(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Car car = (Car) o;

        return state.equals(car.state)
                && carModel.getId() == car.carModel.getId()
                && registrationPlate.equals(car.registrationPlate)
                && color.equals(car.color)
                && pricePerDay.stripTrailingZeros().equals(car.pricePerDay.stripTrailingZeros());
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + carModel.hashCode();
        result = 31 * result + registrationPlate.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    public static class Builder {
        private long id;
        private CarState state;
        private CarModel carModel;
        private String registrationPlate;
        private String color;
        private BigDecimal pricePerDay;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setState(CarState state) {
            this.state = state;
            return this;
        }

        public Builder setCarModel(CarModel carModel) {
            this.carModel = carModel;
            return this;
        }

        public Builder setRegistrationPlate(String registrationPlate) {
            this.registrationPlate = registrationPlate;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setPricePerDay(BigDecimal pricePerDay) {
            this.pricePerDay = pricePerDay;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }

    public static Car createDummyCarWithId(long id) {
        return new Car.Builder()
                .setId(id)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setCarModel(CarModel.createDummyCarModelWithId(-1))
                .setRegistrationPlate("")
                .setColor("")
                .setPricePerDay(BigDecimal.ZERO)
                .build();
    }
}
