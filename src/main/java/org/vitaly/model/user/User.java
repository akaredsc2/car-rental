package org.vitaly.model.user;

import java.time.LocalDate;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-28.
 */
public class User {
    private long id;
    private String login;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String passportNumber;
    private String driverLicenceNumber;
    private UserRole role;

    private User(Builder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.password = builder.password;
        this.fullName = builder.fullName;
        this.birthDate = builder.birthDate;
        this.passportNumber = builder.passportNumber;
        this.driverLicenceNumber = builder.driverLicenceNumber;
        this.role = builder.role;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!login.equals(user.login)) {
            return false;
        }
        if (!password.equals(user.password)) {
            return false;
        }
        if (!fullName.equals(user.fullName)) {
            return false;
        }
        if (!birthDate.equals(user.birthDate)) {
            return false;
        }
        if (!passportNumber.equals(user.passportNumber)) {
            return false;
        }
        if (!driverLicenceNumber.equals(user.driverLicenceNumber)) {
            return false;
        }
        return role == user.role;
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

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setLogin(String login) {
            requireNotNull(login, "Login must not be null!");

            this.login = login;
            return this;
        }

        public Builder setPassword(String password) {
            requireNotNull(password, "Password must not be null!");

            this.password = password;
            return this;
        }

        public Builder setFullName(String fullName) {
            requireNotNull(fullName, "Full name must not be null!");

            this.fullName = fullName;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            requireNotNull(birthDate, "Must not be null!");

            this.birthDate = birthDate;
            return this;
        }

        public Builder setPassportNumber(String passportNumber) {
            requireNotNull(passportNumber, "Passport number must not be null!");

            this.passportNumber = passportNumber;
            return this;
        }

        public Builder setDriverLicenceNumber(String driverLicenceNumber) {
            requireNotNull(driverLicenceNumber, "Driver licence number must not be null!");

            this.driverLicenceNumber = driverLicenceNumber;
            return this;
        }

        public Builder setRole(UserRole role) {
            requireNotNull(role, "User role must not be null!");

            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
