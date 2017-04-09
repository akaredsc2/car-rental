package org.vitaly.dao.implementation;

import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.Mapper;
import org.vitaly.util.dao.mapper.ReservationMapper;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;

import static org.vitaly.util.ExceptionThrower.unsupported;
import static org.vitaly.util.InputChecker.*;

/**
 * Created by vitaly on 2017-04-08.
 */
public class MysqlReservationDao implements ReservationDao {
    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM reservation " +
                    "WHERE reservation_id = ?";
    private static final String CREATE_QUERY =
            "INSERT INTO reservation(" +
                    "client_id, car_id, pick_up_datetime, drop_off_datetime, zone_offset) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY =
            "SELECT * " +
                    "FROM reservation";
    private static final String FIND_RESERVATIONS_BY_CLIENT_ID_QUERY =
            "SELECT * " +
                    "FROM reservation " +
                    "WHERE client_id = ?";
    private static final String FIND_RESERVATIONS_BY_ADMIN_ID_QUERY =
            "SELECT * " +
                    "FROM reservation " +
                    "WHERE admin_id = ?";
    private static final String ADD_ADMIN_TO_RESERVATION_QUERY =
            "UPDATE reservation " +
                    "SET admin_id = ? " +
                    "WHERE reservation_id = ?";
    private static final String CHANGE_RESERVATION_STATE_QUERY =
            "UPDATE reservation " +
                    "SET reservation_status = ? " +
                    "WHERE reservation_id = ?";
    private static final String ADD_REJECTION_REASON_QUERY =
            "UPDATE reservation " +
                    "SET rejection_reason = ? " +
                    "WHERE reservation_id = ?";
    public static final String FIND_RESERVATIONS_WITHOUT_ADMIN_QUERY = "SELECT * FROM reservation WHERE admin_id IS NULL";

    private DaoTemplate daoTemplate;
    private Mapper<Reservation> mapper;

    MysqlReservationDao(PooledConnection connection) {
        this.mapper = new ReservationMapper();
        this.daoTemplate = new DaoTemplate(connection);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, id);

        Reservation reservation = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(reservation);
    }

    @Override
    public Optional<Long> findIdOfEntity(Reservation entity) {
        unsupported();
        return null;
    }

    @Override
    public List<Reservation> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Reservation reservation) {
        requireNotNull(reservation, "Reservation must not be null!");

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, reservation.getClient().getId());
        parameterMap.put(2, reservation.getCar().getId());
        parameterMap.put(3, Timestamp.valueOf(reservation.getPickUpDatetime()));
        parameterMap.put(4, Timestamp.valueOf(reservation.getDropOffDatetime()));
        parameterMap.put(5, ZoneOffset.UTC.toString());

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Reservation entity) {
        unsupported();
        return 0;
    }

    @Override
    public List<Reservation> findReservationsByClientId(long clientId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, clientId);

        return daoTemplate.executeSelect(FIND_RESERVATIONS_BY_CLIENT_ID_QUERY, mapper, parameterMap);
    }

    @Override
    public List<Reservation> findReservationsByAdminId(long adminId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, adminId);

        return daoTemplate.executeSelect(FIND_RESERVATIONS_BY_ADMIN_ID_QUERY, mapper, parameterMap);
    }

    @Override
    public boolean addAdminToReservation(long reservationId, long adminId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, adminId);
        parameterMap.put(2, reservationId);

        return daoTemplate.executeUpdate(ADD_ADMIN_TO_RESERVATION_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean changeReservationState(long reservationId, ReservationState state) {
        requireNotNull(state, "Reservation state must not be null!");

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, state.toString());
        parameterMap.put(2, reservationId);

        return daoTemplate.executeUpdate(CHANGE_RESERVATION_STATE_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean addRejectionReason(long reservationId, String rejectionReason) {
        requireNotNull(rejectionReason, "Rejection reason must not be null!");

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, rejectionReason);
        parameterMap.put(2, reservationId);

        return daoTemplate.executeUpdate(ADD_REJECTION_REASON_QUERY, parameterMap) > 0;
    }

    @Override
    public List<Reservation> findReservationsWithoutAdmin() {
        return daoTemplate.executeSelect(FIND_RESERVATIONS_WITHOUT_ADMIN_QUERY, mapper, Collections.emptyMap());
    }
}
