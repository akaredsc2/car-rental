package org.vitaly.model.user;

import org.vitaly.model.Entity;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.reservation.Reservation;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Created by vitaly on 2017-03-28.
 */
public class User implements Entity {
    private long id;
    private String login;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String passportNumber;
    private String driverLicenceNumber;
    private UserRole role;

    private List<Reservation> reservations;
    private List<Notification> notifications;

    private User(Builder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.password = builder.password;
        this.fullName = builder.fullName;
        this.birthDate = builder.birthDate;
        this.passportNumber = builder.passportNumber;
        this.driverLicenceNumber = builder.driverLicenceNumber;
        this.role = builder.role;

        this.reservations = builder.reservations;
        this.notifications = builder.notifications;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getDriverLicenceNumber() {
        return driverLicenceNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return login.equals(user.login)
                && password.equals(user.password)
                && fullName.equals(user.fullName)
                && birthDate.equals(user.birthDate)
                && passportNumber.equals(user.passportNumber)
                && driverLicenceNumber.equals(user.driverLicenceNumber)
                && role == user.role;
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + birthDate.hashCode();
        result = 31 * result + passportNumber.hashCode();
        result = 31 * result + driverLicenceNumber.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", passportNumber='" + passportNumber + '\'' +
                ", driverLicenceNumber='" + driverLicenceNumber + '\'' +
                ", role=" + role +
                ", reservations=" + reservations +
                ", notifications=" + notifications +
                '}';
    }

    public static class Builder {
        private long id;
        private String login;
        private String password;
        private String fullName;
        private LocalDate birthDate;
        private String passportNumber;
        private String driverLicenceNumber;
        private UserRole role;

        private List<Reservation> reservations;
        private List<Notification> notifications;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setPassportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
            return this;
        }

        public Builder setDriverLicenceNumber(String driverLicenceNumber) {
            this.driverLicenceNumber = driverLicenceNumber;
            return this;
        }

        public Builder setRole(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder setReservations(List<Reservation> reservations) {
            this.reservations = reservations;
            return this;
        }

        public Builder setNotifications(List<Notification> notifications) {
            this.notifications = notifications;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public static User createDummyClientWithId(long id) {
        return new User.Builder()
                .setId(id)
                .setLogin("")
                .setPassword("")
                .setFullName("")
                .setBirthDate(LocalDate.MIN)
                .setPassword("")
                .setDriverLicenceNumber("")
                .setRole(UserRole.CLIENT)
                .setReservations(Collections.emptyList())
                .setNotifications(Collections.emptyList())
                .build();
    }

    public static User createDummyAdminWithId(long id) {
        return new User.Builder()
                .setId(id)
                .setLogin("")
                .setPassword("")
                .setFullName("")
                .setBirthDate(LocalDate.MIN)
                .setPassword("")
                .setDriverLicenceNumber("")
                .setRole(UserRole.ADMIN)
                .setReservations(Collections.emptyList())
                .setNotifications(Collections.emptyList())
                .build();
    }
}
