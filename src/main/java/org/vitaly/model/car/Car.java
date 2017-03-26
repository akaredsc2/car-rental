package org.vitaly.model.car;

import org.vitaly.model.location.Location;
import org.vitaly.util.InputChecker;

import java.math.BigDecimal;

/**
 * Created by vitaly on 2017-03-26.
 */
public class Car {
    private Long id;
    private CarState state;
    private String model;
    private String photoUrl;
    private String color;
    private BigDecimal pricePerDay;
    private Location location;

    private Car(Builder builder) {
        this.id = builder.id;
        this.state = builder.state;
        this.model = builder.model;
        this.photoUrl = builder.photoUrl;
        this.color = builder.color;
        this.pricePerDay = builder.pricePerDay;
        this.location = builder.location;
    }

    public Long getId() {
        return id;
    }

    public CarState getState() {
        return state;
    }

    public String getModel() {
        return model;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getColor() {
        return color;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public Location getLocation() {
        return location;
    }

    void setState(CarState state) {
        this.state = state;
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

    public boolean canMakeAvailable() {
        return state.canMakeAvailable();
    }

    public boolean canMakeUnavailable() {
        return state.canMakeUnavailable();
    }

    public boolean canMaintain() {
        return state.canMaintain();
    }

    public boolean canReserve() {
        return state.canReserve();
    }

    public boolean canServe() {
        return state.canServe();
    }

    public boolean canReturn() {
        return state.canReturn();
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

        if (!state.equals(car.state)) {
            return false;
        }
        if (!model.equals(car.model)) {
            return false;
        }
        if (!photoUrl.equals(car.photoUrl)) {
            return false;
        }
        if (!color.equals(car.color)) {
            return false;
        }
        if (!pricePerDay.equals(car.pricePerDay)) {
            return false;
        }
        return location.equals(car.location);
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + photoUrl.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + pricePerDay.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", state=" + state +
                ", model='" + model + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", color='" + color + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", location=" + location +
                '}';
    }

    public static class Builder {
        private Long id;
        private CarState state;
        private String model;
        private String photoUrl;
        private String color;
        private BigDecimal pricePerDay;
        private Location location;

        public Builder() {
        }

        public Builder setId(Long id) {
            InputChecker.requireNotNull(id, "Id must not be null!");

            this.id = id;
            return this;
        }

        public Builder setState(CarState state) {
            InputChecker.requireNotNull(state, "State must not be null!");

            this.state = state;
            return this;
        }

        public Builder setModel(String model) {
            InputChecker.requireNotNull(model, "Model not be null!");

            this.model = model;
            return this;
        }

        public Builder setPhotoUrl(String photoUrl) {
            InputChecker.requireNotNull(photoUrl, "PhotoUrl must not be null!");

            this.photoUrl = photoUrl;
            return this;
        }

        public Builder setColor(String color) {
            InputChecker.requireNotNull(color, "Color must not be null!");

            this.color = color;
            return this;
        }

        public Builder setPricePerDay(BigDecimal pricePerDay) {
            InputChecker.requireNotNull(pricePerDay, "PricePerDay must not be null!");

            this.pricePerDay = pricePerDay;
            return this;
        }

        public Builder setLocation(Location location) {
            InputChecker.requireNotNull(location, "Location must not be null!");

            this.location = location;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
