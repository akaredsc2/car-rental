package org.vitaly.util.dao.mapper;

import org.junit.Test;
import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-15.
 */
public class ReservationMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<Reservation> mapper = new ReservationMapper();

    @Test

    public void map() throws Exception {
        Reservation expectedReservation = new Reservation.Builder()
                .setId(1L)
                .setClient(User.createDummyClientWithId(2L))
                .setAdmin(User.createDummyAdminWithId(3L))
                .setCar(Car.createDummyCarWithId(4L))
                .setPickUpDatetime(LocalDateTime.now())
                .setDropOffDatetime(LocalDateTime.now().plusDays(1L))
                .setState(ReservationStateEnum.REJECTED.getState())
                .setRejectionReason("reason")
                .build();

        when(resultSet.getLong(RESERVATION_RESERVATION_ID)).thenReturn(expectedReservation.getId());
        when(resultSet.getLong(RESERVATION_CLIENT_ID)).thenReturn(expectedReservation.getClient().getId());
        when(resultSet.getObject(RESERVATION_ADMIN_ID)).thenReturn(expectedReservation.getAdmin().getId());
        when(resultSet.getLong(RESERVATION_CAR_ID)).thenReturn(expectedReservation.getCar().getId());
        when(resultSet.getTimestamp(RESERVATION_PICK_UP_DATETIME))
                .thenReturn(Timestamp.valueOf(expectedReservation.getPickUpDatetime()));
        when(resultSet.getTimestamp(RESERVATION_DROP_OFF_DATETIME))
                .thenReturn(Timestamp.valueOf(expectedReservation.getDropOffDatetime()));
        when(resultSet.getString(RESERVATION_RESERVATION_STATUS)).thenReturn(expectedReservation.getState().toString());
        when(resultSet.getString(RESERVATION_REJECTION_REASON)).thenReturn(expectedReservation.getRejectionReason());

        Reservation actualReservation = mapper.map(resultSet);

        assertThat(actualReservation, allOf(
                equalTo(expectedReservation),
                hasId(expectedReservation.getId())));
    }
}