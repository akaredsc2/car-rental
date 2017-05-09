package org.vitaly.dao.impl.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.BillDao;
import org.vitaly.dao.impl.mysql.factory.ResultSetMapperFactory;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.bill.Bill;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    private static final String MARK_PAID_QUERY =
            "UPDATE bill " +
                    "SET is_paid = ? " +
                    "WHERE bill_id = ?";
    private static final String MARK_CONFIRMED_QUERY =
            "UPDATE bill " +
                    "SET is_confirmed = ? " +
                    "WHERE bill_id = ?";

    private static final String BILL_MUST_NOT_BE_NULL = "Bill must not be null!";

    private static Logger logger = LogManager.getLogger(MysqlBillDao.class.getName());

    @Override
    public Optional<Bill> findById(long id) {
        Mapper<Bill> mapper = ResultSetMapperFactory.getInstance().getBillMapper();
        Bill foundBill = DaoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, Collections.singletonMap(1, id));
        return Optional.ofNullable(foundBill);
    }

    @Override
    public Optional<Long> findIdOfEntity(Bill entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error(e);
        throw e;
    }

    @Override
    public List<Bill> getAll() {
        Mapper<Bill> mapper = ResultSetMapperFactory.getInstance().getBillMapper();
        return DaoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Bill bill) {
        requireNotNull(bill, BILL_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, bill.getDescription().toString());
        parameterMap.put(2, bill.getCashAmount());
        parameterMap.put(3, bill.getCreationDateTime());

        Long createdId = DaoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Bill entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error(e);
        throw e;
    }

    @Override
    public List<Bill> findBillsForReservation(long reservationId) {
        Mapper<Bill> mapper = ResultSetMapperFactory.getInstance().getBillMapper();
        return DaoTemplate
                .executeSelect(FIND_BILLS_FOR_RESERVATION_QUERY, mapper, Collections.singletonMap(1, reservationId));
    }

    @Override
    public boolean addBillToReservation(long billId, long reservationId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, reservationId);
        parameterMap.put(2, billId);

        return DaoTemplate.executeUpdate(ADD_BILL_TO_RESERVATION_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean markPaid(long billId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, true);
        parameterMap.put(2, billId);

        return DaoTemplate.executeUpdate(MARK_PAID_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean markConfirmed(long billId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, true);
        parameterMap.put(2, billId);

        return DaoTemplate.executeUpdate(MARK_CONFIRMED_QUERY, parameterMap) > 0;
    }
}
