USE car_rental;

INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number, role)
VALUES (1, 'admin', 'admin', 'admin', '1995-08-01', 'admin passport', 'admin licence', 'admin');

INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (2, 'caladan', 'brood', 'Caladan Brood', '1975-06-07', '123', '123');
INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (3, 'anomander', 'rake', 'Anomander Rake', '1955-08-19', '234', '234');
INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (4, 'silchas', 'ruin', 'Silchas Ruin', '1957-10-10', '345', '345');
INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (5, 'scara', 'bandaris', 'Scara Bandaris', '1967-04-04', '456', '456');

INSERT INTO model (model_id, model_name, doors, seats, horse_powers)
VALUES (1, 'Chevrolet Corvette', 2, 2, 450);
INSERT INTO model (model_id, model_name, doors, seats, horse_powers)
VALUES (2, 'Volkswagen Corrado', 3, 2, 190);
INSERT INTO model (model_id, model_name, doors, seats, horse_powers)
VALUES (3, 'BMW E60 M5', 4, 4, 500);

INSERT INTO location (location_id, state, city, street, building)
VALUES (1, 'Kiev region', 'Kotsubinske', 'Ponomarjova', '18-a');
INSERT INTO location (location_id, state, city, street, building)
VALUES (2, 'Kharkiv region', 'Kharkiv', 'Druzhbi narodov', '11');
INSERT INTO location (location_id, state, city, street, building)
VALUES (3, 'Mikolaiv region', 'Pervomajsk', 'Rozovaja', '9');

INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (1, 'available', 1, 'plate 1', 'red', 40, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (2, 'available', 1, 'plate 2', 'green', 40, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (3, 'available', 1, 'plate 3', 'black', 40, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (4, 'available', 2, 'plate 4', 'red', 60, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (5, 'unavailable', 2, 'plate 5', 'red', 60, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (6, 'unavailable', 2, 'plate 6', 'red', 60, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (7, 'available', 3, 'plate 7', 'yellow', 30, 3);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (8, 'available', 3, 'plate 8', 'yellow', 30, 3);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (9, 'available', 3, 'plate 9', 'yellow', 30, 3);

