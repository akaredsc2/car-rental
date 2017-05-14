package org.vitaly.model.reservation;

import org.vitaly.model.Entity;
import org.vitaly.model.car.Car;
import org.vitaly.model.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * This class represents reservation for car in system. Reservation
 * can have different states. To create instances of this class use
 * Builder
 *
 * @see ReservationState
 * @see Reservation.Builder
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

    /**
     * Returns id of reservation
     *
     * @return id of reservation
     */
    public long getId() {
        return id;
    }

    /**
     * Returns client who created reservation
     *
     * @return client who created reservation
     */
    public User getClient() {
        return client;
    }

    /**
     * Returns admin who is assigned to this reservation
     *
     * @return admin who is assigned to this reservation
     */
    public User getAdmin() {
        return admin;
    }

    /**
     * Returns car which is reserved
     *
     * @return car which is reserved
     */
    public Car getCar() {
        return car;
    }

    /**
     * Returns local date time for picking up car by client
     *
     * @return local date time for picking up car by client
     */
    public LocalDateTime getPickUpDatetime() {
        return pickUpDatetime;
    }

    /**
     * Returns local date time for dropping of car by client
     *
     * @return local date time for dropping of car by client
     */
    public LocalDateTime getDropOffDatetime() {
        return dropOffDatetime;
    }

    /**
     * Returns state of reservation
     *
     * @return state of reservation
     */
    public ReservationState getState() {
        return state;
    }

    /**
     * Sets state of reservation. Part of State design pattern
     *
     * @param state new state of reservation
     */
    void setState(ReservationState state) {
        this.state = state;
    }

    /**
     * Returns rejection reason for reservation
     *
     * @return rejection reason for reservation
     */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /**
     * Try to approve reservation. Part of State design pattern
     *
     * @return true is reservation been approved and false otherwise
     */
    public boolean approve() {
        return state.approve(this);
    }

    /**
     * Try to reject reservation. Part of State design pattern
     *
     * @return true is reservation been rejected and false otherwise
     */
    public boolean reject() {
        return state.reject(this);
    }

    /**
     * Try to cancel reservation. Part of State design pattern
     *
     * @return true is reservation been canceled and false otherwise
     */
    public boolean cancel() {
        return state.cancel(this);
    }

    /**
     * Try to activate reservation. Part of State design pattern
     *
     * @return true is reservation been activated and false otherwise
     */
    public boolean activate() {
        return state.activate(this);
    }

    /**
     * Try to close reservation. Part of State design pattern
     *
     * @return true is reservation been closed and false otherwise
     */
    public boolean close() {
        return state.close(this);
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

        return Objects.equals(this.client.getId(), that.client.getId())
                && Objects.equals(this.car.getId(), that.car.getId())
                && pickUpDatetime.until(that.pickUpDatetime, ChronoUnit.SECONDS) == 0
                && dropOffDatetime.until(that.dropOffDatetime, ChronoUnit.SECONDS) == 0
                && state.equals(that.state);
    }

    @Override
    public int hashCode() {
        int result = client.hashCode();
        result = 31 * result + car.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + client +
                ", admin=" + admin +
                ", car=" + car +
                ", pickUpDatetime=" + pickUpDatetime +
                ", dropOffDatetime=" + dropOffDatetime +
                ", state=" + state +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }

    /**
     * This class creates instances of Reservation. Use setter chain to
     * set desired parameters and build methods to create instance
     * of Reservation with supplied parameters.
     *
     * @see Reservation
     */
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
            this.client = client;
            return this;
        }

        public Builder setAdmin(User admin) {
            this.admin = admin;
            return this;
        }

        public Builder setCar(Car car) {
            this.car = car;
            return this;
        }

        public Builder setPickUpDatetime(LocalDateTime pickUpDatetime) {
            this.pickUpDatetime = pickUpDatetime;
            return this;
        }

        public Builder setDropOffDatetime(LocalDateTime dropOffDatetime) {
            this.dropOffDatetime = dropOffDatetime;
            return this;
        }

        public Builder setState(ReservationState state) {
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
