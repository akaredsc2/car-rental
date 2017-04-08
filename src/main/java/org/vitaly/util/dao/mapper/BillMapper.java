package org.vitaly.util.dao.mapper;

import org.vitaly.model.bill.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by vitaly on 2017-04-08.
 */
public class BillMapper implements Mapper<Bill> {
    @Override
    public Bill map(ResultSet resultSet) throws SQLException {
        LocalDateTime creationDateTime = resultSet.getTimestamp("bill.creation_datetime").toLocalDateTime();

        return new Bill.Builder()
                .setId(resultSet.getLong("bill.bill_id"))
                .setPaid(resultSet.getBoolean("bill.is_paid"))
                .setDescription(resultSet.getString("bill.description"))
                .setCashAmount(resultSet.getBigDecimal("bill.cash_amount"))
                .setCreationDateTime(creationDateTime)
                .build();
    }
}
