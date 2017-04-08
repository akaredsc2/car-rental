package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.Mapper;
import org.vitaly.util.dao.mapper.ReservationMapper;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by vitaly on 2017-04-08.
 */
public class MysqlReservationDao implements ReservationDao {
    public static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM reservation " +
                    "WHERE reservation_id = ?";
    public static final String CREATE_QUERY =
            "INSERT INTO reservation(" +
                    "client_id, car_id, pick_up_datetime, drop_off_datetime, zone_offset) " +
                    "VALUES (?, ?, ?, ?, ?)";
    public static final String GET_ALL_QUERY =
            "SELECT * " +
                    "FROM reservation";
    public static final String FIND_RESERVATIONS_BY_CLIENT_ID_QUERY =
            "SELECT * " +
                    "FROM reservation " +
                    "WHERE client_id = ?";
    public static final String FIND_RESERVATIONS_BY_ADMIN_ID_QUERY =
            "SELECT * " +
                    "FROM reservation " +
                    "WHERE admin_id = ?";
    public static final String ADD_ADMIN_TO_RESERVATION_QUERY =
            "UPDATE reservation " +
                    "SET admin_id = ? " +
                    "WHERE reservation_id = ?";
    public static final String CHANGE_RESERVATION_STATE_QUERY =
            "UPDATE reservation " +
                    "SET reservation_status = ? " +
                    "WHERE reservation_id = ?";

    private static final Logger logger = LogManager.getLogger(MysqlNotificationDao.class.getName());
    public static final String ADD_REJECTION_REASON_QUERY = "UPDATE reservation SET rejection_reason = ? WHERE reservation_id = ?";

    private PooledConnection connection;
    private DaoTemplate daoTemplate;
    private Mapper<Reservation> mapper;

    public MysqlReservationDao(PooledConnection connection) {
        this.connection = connection;
        this.mapper = new ReservationMapper();
        this.daoTemplate = new DaoTemplate(connection);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, id);

        Reservation reservation = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(reservation);
    }

    @Override
    public Optional<Long> findIdOfEntity(Reservation entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error("Error while calling unsupported operation.", e);
        throw e;
    }

    @Override
    public List<Reservation> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, new TreeMap<>());
    }

    @Override
    public Optional<Long> create(Reservation reservation) {
        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
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
        RuntimeException e = new UnsupportedOperationException();
        logger.error("Error while calling unsupported operation.", e);
        throw e;
    }

    @Override
    public List<Reservation> findReservationsByClientId(long clientId) {
        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, clientId);

        return daoTemplate.executeSelect(FIND_RESERVATIONS_BY_CLIENT_ID_QUERY, mapper, parameterMap);
    }

    @Override
    public List<Reservation> findReservationsByAdminId(long adminId) {
        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, adminId);

        return daoTemplate.executeSelect(FIND_RESERVATIONS_BY_ADMIN_ID_QUERY, mapper, parameterMap);
    }

    @Override
    public boolean addAdminToReservation(long reservationId, long adminId) {
        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, adminId);
        parameterMap.put(2, reservationId);

        return daoTemplate.executeUpdate(ADD_ADMIN_TO_RESERVATION_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean changeReservationState(long reservationId, ReservationState state) {
        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, state.toString());
        parameterMap.put(2, reservationId);

        return daoTemplate.executeUpdate(CHANGE_RESERVATION_STATE_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean addRejectionReason(long reservationId, String rejectionReason) {
        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, rejectionReason);
        parameterMap.put(2, reservationId);

        return daoTemplate.executeUpdate(ADD_REJECTION_REASON_QUERY, parameterMap) > 0;
    }
}
