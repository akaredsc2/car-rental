package org.vitaly.service.impl.dto;

import org.vitaly.model.user.UserRole;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by vitaly on 2017-04-20.
 */
public class UserDto {
    private long id;
    private String login;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String passportNumber;
    private String driverLicenceNumber;
    private UserRole role;

    private List<ReservationDto> reservationDtoList;
    private List<NotificationDto> notificationDtoList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getDriverLicenceNumber() {
        return driverLicenceNumber;
    }

    public void setDriverLicenceNumber(String driverLicenceNumber) {
        this.driverLicenceNumber = driverLicenceNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<ReservationDto> getReservationDtoList() {
        return reservationDtoList;
    }

    public UserDto setReservationDtoList(List<ReservationDto> reservationDtoList) {
        this.reservationDtoList = reservationDtoList;
        return this;
    }

    public List<NotificationDto> getNotificationDtoList() {
        return notificationDtoList;
    }

    public UserDto setNotificationDtoList(List<NotificationDto> notificationDtoList) {
        this.notificationDtoList = notificationDtoList;
        return this;
    }
}
