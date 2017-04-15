package org.vitaly.model.reservation;

import org.vitaly.model.Entity;
import org.vitaly.model.car.Car;
import org.vitaly.model.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-04-08.
 */
public class Reservation implements Entity {
    private long id;
    private User client;
    private User admin;
    private Car car;
    private LocalDateTime pickUpDatetime;
    private LocalDateTime dropOffDatetime;
    private ReservationState state;
    private String rejectionReason;

    private Reservation(Builder builder) {
        this.id = builder.id;
        this.client = builder.client;
        this.admin = builder.admin;
        this.car = builder.car;
        this.pickUpDatetime = builder.pickUpDatetime;
        this.dropOffDatetime = builder.dropOffDatetime;
        this.state = builder.state;
        this.rejectionReason = builder.rejectionReason;
    }

    public long getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public User getAdmin() {
        return admin;
    }

    public Car getCar() {
        return car;
    }

    public LocalDateTime getPickUpDatetime() {
        return pickUpDatetime;
    }

    public LocalDateTime getDropOffDatetime() {
        return dropOffDatetime;
    }

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reservation that = (Reservation) o;

        if (!Objects.equals(this.client.getId(), that.client.getId())) {
            return false;
        }
        if (admin != null ? !Objects.equals(this.admin.getId(), that.admin.getId()) : that.admin != null) {
            return false;
        }
        if (!Objects.equals(this.car.getId(), that.car.getId())) {
            return false;
        }
        if (pickUpDatetime.until(that.pickUpDatetime, ChronoUnit.SECONDS) != 0) {
            return false;
        }
        if (dropOffDatetime.until(that.dropOffDatetime, ChronoUnit.SECONDS) != 0) {
            return false;
        }
        if (!state.equals(that.state)) {
            return false;
        }
        return rejectionReason != null ? rejectionReason.equals(that.rejectionReason) : that.rejectionReason == null;
    }

    @Override
    public int hashCode() {
        int result = client.hashCode();
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        result = 31 * result + car.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + (rejectionReason != null ? rejectionReason.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + client +
                ", admin=" + admin +
                ", car=" + car.getId() +
                ", pickUpDatetime=" + pickUpDatetime +
                ", dropOffDatetime=" + dropOffDatetime +
                ", state=" + state +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }

    public static class Builder {
        private long id;
        private User client;
        private User admin;
        private Car car;
        private LocalDateTime pickUpDatetime;
        private LocalDateTime dropOffDatetime;
        private ReservationState state;
        private String rejectionReason;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setClient(User client) {
            requireNotNull(client, "Client must not be null!");

            this.client = client;
            return this;
        }

        public Builder setAdmin(User admin) {
            this.admin = admin;
            return this;
        }

        public Builder setCar(Car car) {
            requireNotNull(car, "Car must not be null!");

            this.car = car;
            return this;
        }

        public Builder setPickUpDatetime(LocalDateTime pickUpDatetime) {
            requireNotNull(pickUpDatetime, "Pick up datetime must not be null!");

            this.pickUpDatetime = pickUpDatetime;
            return this;
        }

        public Builder setDropOffDatetime(LocalDateTime dropOffDatetime) {
            requireNotNull(dropOffDatetime, "Drop off datetime must not be null!");

            this.dropOffDatetime = dropOffDatetime;
            return this;
        }

        public Builder setState(ReservationState state) {
            requireNotNull(state, "Reservation state must not be null!");

            this.state = state;
            return this;
        }

        public Builder setRejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }
}
