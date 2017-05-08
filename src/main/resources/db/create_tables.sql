SET GLOBAL time_zone = '+03:00';
SET NAMES 'utf8';
SET CHARACTER SET 'utf8';

DROP SCHEMA car_rental;
DROP SCHEMA car_rental_test;

CREATE SCHEMA car_rental;
CREATE SCHEMA car_rental_test;

USE car_rental;
USE car_rental_test;

CREATE TABLE users (
  user_id               BIGINT                   NOT NULL AUTO_INCREMENT,
  login                 VARCHAR(20)              NOT NULL,
  pass                  VARCHAR(20)              NOT NULL,
  full_name             VARCHAR(50)              NOT NULL,
  birth_date            DATE                     NOT NULL,
  passport_number       VARCHAR(20)              NOT NULL,
  driver_licence_number VARCHAR(20)              NOT NULL,
  role                  ENUM ('client', 'admin') NOT NULL DEFAULT 'client',

  PRIMARY KEY (user_id)
);
ALTER TABLE users
  ADD CONSTRAINT u_users_login UNIQUE (login);
ALTER TABLE users
  ADD CONSTRAINT u_users_passport_number UNIQUE (passport_number);
ALTER TABLE users
  ADD CONSTRAINT u_users_driver_licence_number UNIQUE (driver_licence_number);

CREATE TABLE notification (
  notification_id       BIGINT                 NOT NULL AUTO_INCREMENT,
  user_id               BIGINT                          DEFAULT NULL,
  notification_datetime DATETIME               NOT NULL,
  notification_status   ENUM ('new', 'viewed') NOT NULL DEFAULT 'new',
  header                VARCHAR(128)           NOT NULL,
  content               TEXT                   NOT NULL,

  PRIMARY KEY (notification_id)
);
ALTER TABLE notification
  ADD CONSTRAINT fk_notification_users FOREIGN KEY (user_id) REFERENCES users (user_id);

CREATE TABLE location (
  location_id BIGINT      NOT NULL AUTO_INCREMENT,
  state       VARCHAR(20) NOT NULL,
  city        VARCHAR(20) NOT NULL,
  street      VARCHAR(20) NOT NULL,
  building    VARCHAR(20) NOT NULL,
  photo_url   VARCHAR(128)         DEFAULT NULL,

  PRIMARY KEY (location_id)
);
ALTER TABLE location
  ADD CONSTRAINT u_location UNIQUE (state, city, street, building);

CREATE TABLE model (
  model_id     BIGINT      NOT NULL AUTO_INCREMENT,
  model_name   VARCHAR(50) NOT NULL,
  photo_url    VARCHAR(128)         DEFAULT NULL,
  doors        INTEGER     NOT NULL,
  seats        INTEGER     NOT NULL,
  horse_powers INTEGER     NOT NULL,

  PRIMARY KEY (model_id)
);
ALTER TABLE model
  ADD CONSTRAINT u_model UNIQUE (model_name);
ALTER TABLE model
  ADD CONSTRAINT c_model_doors CHECK (doors >= 0 AND doors <= 10);
ALTER TABLE model
  ADD CONSTRAINT c_model_seats CHECK (seats >= 1 AND seats <= 100);
ALTER TABLE model
  ADD CONSTRAINT c_model_horse_powers CHECK (horse_powers >= 0 AND horse_powers <= 3000);

CREATE TABLE car (
  car_id             BIGINT               NOT NULL AUTO_INCREMENT,
  car_status         ENUM ('available',
                           'reserved',
                           'served',
                           'returned',
                           'unavailable') NOT NULL DEFAULT 'unavailable',
  model_id           BIGINT               NOT NULL,
  registration_plate VARCHAR(20)          NOT NULL,
  color              VARCHAR(20)          NOT NULL,
  price_per_day      DECIMAL(10, 2)       NOT NULL,
  location_id        BIGINT                        DEFAULT NULL,

  PRIMARY KEY (car_id)
);
ALTER TABLE car
  ADD CONSTRAINT u_car UNIQUE (registration_plate);
ALTER TABLE car
  ADD CONSTRAINT fk_car_location FOREIGN KEY (location_id) REFERENCES location (location_id);
ALTER TABLE car
  ADD CONSTRAINT fk_car_model FOREIGN KEY (model_id) REFERENCES model (model_id);

CREATE TABLE reservation (
  reservation_id     BIGINT          NOT NULL AUTO_INCREMENT,
  client_id          BIGINT          NOT NULL,
  admin_id           BIGINT                   DEFAULT NULL,
  car_id             BIGINT          NOT NULL,
  pick_up_datetime   DATETIME        NOT NULL,
  drop_off_datetime  DATETIME        NOT NULL,
  reservation_status ENUM ('new',
                           'approved',
                           'rejected',
                           'canceled',
                           'active',
                           'closed') NOT NULL DEFAULT 'new',
  rejection_reason   VARCHAR(128)             DEFAULT NULL,

  PRIMARY KEY (reservation_id)
);
ALTER TABLE reservation
  ADD CONSTRAINT fk_reservation_client FOREIGN KEY (client_id) REFERENCES users (user_id);
ALTER TABLE reservation
  ADD CONSTRAINT fk_reservation_admin FOREIGN KEY (admin_id) REFERENCES users (user_id);
ALTER TABLE reservation
  ADD CONSTRAINT fk_reservation_car FOREIGN KEY (car_id) REFERENCES car (car_id);

CREATE TABLE bill (
  bill_id           BIGINT         NOT NULL AUTO_INCREMENT,
  is_paid           BOOLEAN        NOT NULL DEFAULT FALSE,
  is_confirmed      BOOLEAN        NOT NULL DEFAULT FALSE,
  reservation_id    BIGINT                  DEFAULT NULL,
  description       VARCHAR(128)   NOT NULL,
  cash_amount       DECIMAL(10, 2) NOT NULL,
  creation_datetime DATETIME       NOT NULL,

  PRIMARY KEY (bill_id)
);
ALTER TABLE bill
  ADD CONSTRAINT fk_bill_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (reservation_id);
ALTER TABLE bill
  ADD CONSTRAINT c_bill_cash_amount CHECK (cash_amount > 0);
