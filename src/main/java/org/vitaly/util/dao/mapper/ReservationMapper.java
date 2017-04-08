package org.vitaly.util.dao.mapper;

import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by vitaly on 2017-04-08.
 */
public class ReservationMapper implements Mapper<Reservation> {
    @Override
    public Reservation map(ResultSet resultSet) throws SQLException {

        // TODO: 2017-04-08 extract factory method
        User client = new User.Builder()
                .setId(resultSet.getLong("reservation.client_id"))
                .setLogin("")
                .setPassword("")
                .setFullName("")
                .setBirthDate(LocalDate.MIN)
                .setPassword("")
                .setDriverLicenceNumber("")
                .setRole(UserRole.CLIENT)
                .build();

        // TODO: 2017-04-08 possible npe
        Object adminId = resultSet.getObject("reservation.admin_id");

        User admin = null;
        if (adminId != null) {
            admin = new User.Builder()
                    .setId((Long) adminId)
                    .setLogin("")
                    .setPassword("")
                    .setFullName("")
                    .setBirthDate(LocalDate.MIN)
                    .setPassword("")
                    .setDriverLicenceNumber("")
                    .setRole(UserRole.ADMIN)
                    .build();
        }

        Car car = new Car.Builder()
                .setId(resultSet.getLong("reservation.car_id"))
                .build();

        // TODO: 2017-04-08 apply zone offset
        LocalDateTime pickUpDatetime = resultSet.getTimestamp("reservation.pick_up_datetime").toLocalDateTime();
        LocalDateTime dropOffDatetime = resultSet.getTimestamp("reservation.drop_off_datetime").toLocalDateTime();

        ReservationState state = ReservationStateEnum.valueOf(
                resultSet.getString("reservation.reservation_status").toUpperCase()).getState();

        return new Reservation.Builder()
                .setId(resultSet.getLong("reservation.reservation_id"))
                .setClient(client)
                .setAdmin(admin)
                .setCar(car)
                .setPickUpDatetime(pickUpDatetime)
                .setDropOffDatetime(dropOffDatetime)
                .setState(state)
                .setRejectionReason(resultSet.getString("reservation.rejection_reason"))
                .build();
    }
}
