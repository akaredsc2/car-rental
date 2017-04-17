package org.vitaly.service.implementation;

import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.abstraction.BillDao;
//import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.model.bill.Bill;
import org.vitaly.service.abstraction.BillService;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public class BillServiceImpl implements BillService {
    private BillDao dao;

    public BillServiceImpl(PooledConnection connection) {
//        this.dao = DaoFactory.getMysqlDaoFactory().createBillDao(connection);
    }

    @Override
    public Optional<Bill> findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Bill> getAll() {
        return dao.getAll();
    }

    @Override
    public Optional<Long> create(Bill bill) {
        return dao.create(bill);
    }

    @Override
    public List<Bill> findBillsForReservation(long reservationId) {
        return dao.findBillsForReservation(reservationId);
    }

    @Override
    public boolean addBillToReservation(long billId, long reservationId) {
        return dao.addBillToReservation(billId, reservationId);
    }

    @Override
    public boolean markAsPaid(long billId) {
        return dao.markAsPaid(billId);
    }
}
