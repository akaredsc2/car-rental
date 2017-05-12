USE car_rental;

INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number, role)
VALUES (1, 'admin', 'admin', 'Admin Admin', '1995-08-01', 'АА111111', 'ААА111111', 'admin');

INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (2, 'caladan', 'brood', 'Caladan Brood', '1975-06-07', 'ББ111111', '11ББ111111');
INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (3, 'anomander', 'rake', 'Anomander Rake', '1955-08-19', '0123456789', 'ВВВ111111');
INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (4, 'silchas', 'ruin', 'Silchas Ruin', '1957-10-10', 'ГГ111111', '11ГГ111111');
INSERT INTO users (user_id, login, pass, full_name, birth_date, passport_number, driver_licence_number)
VALUES (5, 'scara', 'bandaris', 'Scara Bandaris', '1967-04-04', '9876543210', 'ДДД111111');

INSERT INTO model (model_id, model_name, doors, seats, horse_powers)
VALUES (1, 'Chevrolet Corvette', 2, 2, 450);
INSERT INTO model (model_id, model_name, doors, seats, horse_powers)
VALUES (2, 'Volkswagen Corrado', 3, 2, 190);
INSERT INTO model (model_id, model_name, doors, seats, horse_powers)
VALUES (3, 'BMW E60 M5', 4, 4, 500);

INSERT INTO location (location_id, state, city, street, building)
VALUES (1, 'Kiev region', 'Kotsubinske', 'Ponomarjova', '18');
INSERT INTO location (location_id, state, city, street, building)
VALUES (2, 'Kharkiv region', 'Kharkiv', 'Druzhbi narodov', '11');
INSERT INTO location (location_id, state, city, street, building)
VALUES (3, 'Mikolaiv region', 'Pervomajsk', 'Rozovaja', '9');

INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (1, 'available', 1, 'АА1111АА', 'red', 40, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (2, 'available', 1, 'ББ1111АА', 'green', 40, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (3, 'available', 1, 'ВВ1111АА', 'black', 40, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (4, 'available', 2, 'ГГ1111АА', 'red', 60, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (5, 'unavailable', 2, 'ДД1111АА', 'red', 60, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (6, 'unavailable', 2, 'ЕЕ1111АА', 'red', 60, 1);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (7, 'available', 3, 'ЖЖ1111АА', 'yellow', 30, 3);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (8, 'available', 3, 'ЗЗ1111АА', 'yellow', 30, 3);
INSERT INTO car (car_id, car_status, model_id, registration_plate, color, price_per_day, location_id)
VALUES (9, 'available', 3, 'ИИ1111АА', 'yellow', 30, 3);
