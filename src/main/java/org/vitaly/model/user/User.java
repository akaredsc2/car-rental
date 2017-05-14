package org.vitaly.model.user;

import org.vitaly.model.Entity;

import java.time.LocalDate;

/**
 * This class represents users of system. For creating instances of this class
 * use static factory methods or builder
 *
 * @see User#createDummyClientWithId(long)
 * @see User#createDummyAdminWithId(long)
 * @see User.Builder
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

    /**
     * Returns id of user
     *
     * @return id of user
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * Returns login of user
     *
     * @return login of user
     */
    public String getLogin() {
        return login;
    }

    /**
     * Returns password of user
     *
     * @return password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns full name of user
     *
     * @return full name of user
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns birth date of user
     *
     * @return birth date of user
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns passport number of user
     *
     * @return passport number of user
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * Returns passport number of user
     *
     * @return passport number of user
     */
    public String getDriverLicenceNumber() {
        return driverLicenceNumber;
    }

    /**
     * Returns role of user
     *
     * @return role of user
     */
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
                '}';
    }

    /**
     * This class is used to create instance of User class with desired
     * parameters. To create User call build() method. Invoke setters
     * in chain to set desired parameters.
     *
     * @see User
     */
    public static class Builder {
        private long id;
        private String login;
        private String password;
        private String fullName;
        private LocalDate birthDate;
        private String passportNumber;
        private String driverLicenceNumber;
        private UserRole role;

        /**
         * Sets desired user id
         *
         * @param id desired user id
         * @return builder with desired id
         */
        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        /**
         * Sets desired user login
         *
         * @param login desired user login
         * @return builder with desired login
         */
        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        /**
         * Sets desired user password
         *
         * @param password desired user password
         * @return builder with desired password
         */
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Sets desired user full name
         *
         * @param fullName desired user full name
         * @return builder with desired full name
         */
        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        /**
         * Sets desired user birth date
         *
         * @param birthDate desired user birth date
         * @return builder with desired birth date
         */
        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        /**
         * Sets desired user passport number
         *
         * @param passportNumber desired user passport number
         * @return builder with desired passport number
         */
        public Builder setPassportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
            return this;
        }

        /**
         * Sets desired user driver licence number
         *
         * @param driverLicenceNumber desired user driver licence number
         * @return builder with desired driver licence number
         */
        public Builder setDriverLicenceNumber(String driverLicenceNumber) {
            this.driverLicenceNumber = driverLicenceNumber;
            return this;
        }

        /**
         * Sets desired user role
         *
         * @param role desired user role
         * @return builder with desired user role
         */
        public Builder setRole(UserRole role) {
            this.role = role;
            return this;
        }

        /**
         * Creates user with parameters supplied
         * using setter chain and default values otherwise
         *
         * @return User with supplied parameters
         */
        public User build() {
            return new User(this);
        }
    }

    /**
     * Static factory method to create user with concrete id and client role.
     * No guarantee on values of other fields other than that they are not null.
     *
     * @param id id of created dummy
     * @return User with supplied id and client role
     */
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
                .build();
    }

    /**
     * Static factory method to create user with concrete id and admin role.
     * No guarantee on values of other fields other than that they are not null.
     *
     * @param id id of created dummy
     * @return User with supplied id and admin role
     */
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
                .build();
    }
}
