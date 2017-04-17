package org.vitaly.dao.impl.mysql;

import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.model.bill.Bill;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.BillMapper;
import org.vitaly.util.dao.mapper.Mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.vitaly.util.ExceptionThrower.unsupported;
import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-04-08.
 */
public class MysqlBillDao implements BillDao {
    private static final String CREATE_QUERY =
            "INSERT INTO bill(description, cash_amount, creation_datetime) " +
                    "VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM bill " +
                    "WHERE bill_id = ?";
    private static final String GET_ALL_QUERY =
            "SELECT * " +
                    "FROM bill";
    private static final String FIND_BILLS_FOR_RESERVATION_QUERY =
            "SELECT * " +
                    "FROM bill " +
                    "WHERE reservation_id = ?";
    private static final String ADD_BILL_TO_RESERVATION_QUERY =
            "UPDATE bill " +
                    "SET reservation_id = ? " +
                    "WHERE bill_id = ?";
    private static final String MARK_AS_PAID_QUERY =
            "UPDATE bill " +
                    "SET is_paid = ? " +
                    "WHERE bill_id = ?";

    private DaoTemplate daoTemplate;
    private Mapper<Bill> mapper;

    public MysqlBillDao(PooledConnection connection) {
        this.daoTemplate = new DaoTemplate(connection);
        this.mapper = new BillMapper();
    }

    @Override
    public Optional<Bill> findById(long id) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, id);

        Bill foundBill = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(foundBill);
    }

    @Override
    public Optional<Long> findIdOfEntity(Bill entity) {
        unsupported();
        return null;
    }

    @Override
    public List<Bill> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Bill bill) {
        requireNotNull(bill, "Bill must not be null!");

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, bill.getDescription());
        parameterMap.put(2, bill.getCashAmount());
        parameterMap.put(3, bill.getCreationDateTime());

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Bill entity) {
        unsupported();
        return 0;
    }

    @Override
    public List<Bill> findBillsForReservation(long reservationId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, reservationId);

        return daoTemplate.executeSelect(FIND_BILLS_FOR_RESERVATION_QUERY, mapper, parameterMap);
    }

    @Override
    public boolean addBillToReservation(long billId, long reservationId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, reservationId);
        parameterMap.put(2, billId);

        return daoTemplate.executeUpdate(ADD_BILL_TO_RESERVATION_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean markAsPaid(long billId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, true);
        parameterMap.put(2, billId);

        return daoTemplate.executeUpdate(MARK_AS_PAID_QUERY, parameterMap) > 0;
    }
}
