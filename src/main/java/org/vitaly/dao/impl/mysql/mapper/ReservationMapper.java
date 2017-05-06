package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-08.
 */
public class ReservationMapper implements Mapper<Reservation> {

    @Override
    public Reservation map(ResultSet resultSet) throws SQLException {
        User client = User.createDummyClientWithId(resultSet.getLong(RESERVATION_CLIENT_ID));

        Object adminId = resultSet.getObject(RESERVATION_ADMIN_ID);
        User admin = null;
        if (adminId != null) {
            admin = User.createDummyAdminWithId((Long) adminId);
        }

        Car car = Car.createDummyCarWithId(resultSet.getLong(RESERVATION_CAR_ID));

        LocalDateTime pickUpDatetime = resultSet.getTimestamp(RESERVATION_PICK_UP_DATETIME).toLocalDateTime();
        LocalDateTime dropOffDatetime = resultSet.getTimestamp(RESERVATION_DROP_OFF_DATETIME).toLocalDateTime();

        ReservationState state = ReservationStateEnum.valueOf(
                resultSet.getString(RESERVATION_RESERVATION_STATUS).toUpperCase()).getState();

        return new Reservation.Builder()
                .setId(resultSet.getLong(RESERVATION_RESERVATION_ID))
                .setClient(client)
                .setAdmin(admin)
                .setCar(car)
                .setPickUpDatetime(pickUpDatetime)
                .setDropOffDatetime(dropOffDatetime)
                .setState(state)
                .setRejectionReason(resultSet.getString(RESERVATION_REJECTION_REASON))
                .build();
    }
}
