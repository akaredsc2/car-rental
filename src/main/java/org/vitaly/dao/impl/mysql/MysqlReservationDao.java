package org.vitaly.dao.impl.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.ReservationDao;
import org.vitaly.dao.impl.mysql.factory.ResultSetMapperFactory;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.vitaly.util.InputChecker.requireNotNull;

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
                    "client_id, car_id, pick_up_datetime, drop_off_datetime) " +
                    "VALUES (?, ?, ?, ?)";
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
    private static final String FIND_RESERVATIONS_WITHOUT_ADMIN_QUERY =
            "SELECT * " +
                    "FROM reservation " +
                    "WHERE admin_id IS NULL";

    private static final String RESERVATION_MUST_NOT_BE_NULL = "Reservation must not be null!";
    private static final String REJECTION_REASON_MUST_NOT_BE_NULL = "Rejection reason must not be null!";

    private static Logger logger = LogManager.getLogger(MysqlReservationDao.class.getName());

    @Override
    public Optional<Reservation> findById(long id) {
        Mapper<Reservation> mapper = ResultSetMapperFactory.getInstance().getReservationMapper();
        Reservation reservation = DaoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, Collections.singletonMap(1, id));
        return Optional.ofNullable(reservation);
    }

    @Override
    public Optional<Long> findIdOfEntity(Reservation entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error(e);
        throw e;
    }

    @Override
    public List<Reservation> getAll() {
        Mapper<Reservation> mapper = ResultSetMapperFactory.getInstance().getReservationMapper();
        return DaoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Reservation reservation) {
        requireNotNull(reservation, RESERVATION_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, reservation.getClient().getId());
        parameterMap.put(2, reservation.getCar().getId());
        parameterMap.put(3, Timestamp.valueOf(reservation.getPickUpDatetime()));
        parameterMap.put(4, Timestamp.valueOf(reservation.getDropOffDatetime()));

        Long createdId = DaoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Reservation entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error(e);
        throw e;
    }

    @Override
    public List<Reservation> findReservationsByClientId(long clientId) {
        Mapper<Reservation> mapper = ResultSetMapperFactory.getInstance().getReservationMapper();
        return DaoTemplate
                .executeSelect(FIND_RESERVATIONS_BY_CLIENT_ID_QUERY, mapper, Collections.singletonMap(1, clientId));
    }

    @Override
    public List<Reservation> findReservationsByAdminId(long adminId) {
        Mapper<Reservation> mapper = ResultSetMapperFactory.getInstance().getReservationMapper();
        return DaoTemplate
                .executeSelect(FIND_RESERVATIONS_BY_ADMIN_ID_QUERY, mapper, Collections.singletonMap(1, adminId));
    }

    @Override
    public boolean addAdminToReservation(long reservationId, long adminId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, adminId);
        parameterMap.put(2, reservationId);

        return DaoTemplate.executeUpdate(ADD_ADMIN_TO_RESERVATION_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean changeReservationState(long reservationId, ReservationState state) {
        requireNotNull(state, RESERVATION_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, state.toString());
        parameterMap.put(2, reservationId);

        return DaoTemplate.executeUpdate(CHANGE_RESERVATION_STATE_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean addRejectionReason(long reservationId, String rejectionReason) {
        requireNotNull(rejectionReason, REJECTION_REASON_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, rejectionReason);
        parameterMap.put(2, reservationId);

        return DaoTemplate.executeUpdate(ADD_REJECTION_REASON_QUERY, parameterMap) > 0;
    }

    @Override
    public List<Reservation> findReservationsWithoutAdmin() {
        Mapper<Reservation> mapper = ResultSetMapperFactory.getInstance().getReservationMapper();
        return DaoTemplate.executeSelect(FIND_RESERVATIONS_WITHOUT_ADMIN_QUERY, mapper, Collections.emptyMap());
    }
}
