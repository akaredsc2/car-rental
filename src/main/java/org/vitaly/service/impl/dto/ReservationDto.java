package org.vitaly.service.impl.dto;

import org.vitaly.model.reservation.ReservationState;

import java.time.LocalDateTime;

/**
 * Created by vitaly on 2017-04-20.
 */
public class ReservationDto {
    private long id;
    private UserDto client;
    private UserDto admin;
    private CarDto car;
    private LocalDateTime pickUpDatetime;
    private LocalDateTime dropOffDatetime;
    private ReservationState state;
    private String rejectionReason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDto getClient() {
        return client;
    }

    public void setClient(UserDto client) {
        this.client = client;
    }

    public UserDto getAdmin() {
        return admin;
    }

    public void setAdmin(UserDto admin) {
        this.admin = admin;
    }

    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
    }

    public LocalDateTime getPickUpDatetime() {
        return pickUpDatetime;
    }

    public void setPickUpDatetime(LocalDateTime pickUpDatetime) {
        this.pickUpDatetime = pickUpDatetime;
    }

    public LocalDateTime getDropOffDatetime() {
        return dropOffDatetime;
    }

    public void setDropOffDatetime(LocalDateTime dropOffDatetime) {
        this.dropOffDatetime = dropOffDatetime;
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

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
