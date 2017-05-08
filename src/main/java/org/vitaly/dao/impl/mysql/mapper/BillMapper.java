package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.model.bill.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-08.
 */
public class BillMapper implements Mapper<Bill> {

    @Override
    public Bill map(ResultSet resultSet) throws SQLException {
        LocalDateTime creationDateTime = resultSet.getTimestamp(BILL_CREATION_DATETIME).toLocalDateTime();

        return new Bill.Builder()
                .setId(resultSet.getLong(BILL_BILL_ID))
                .setPaid(resultSet.getBoolean(BILL_IS_PAID))
                .setConfirmed(resultSet.getBoolean(BILL_IS_CONFIRMED))
                .setDescription(resultSet.getString(BILL_DESCRIPTION))
                .setCashAmount(resultSet.getBigDecimal(BILL_CASH_AMOUNT))
                .setCreationDateTime(creationDateTime)
                .build();
    }
}
