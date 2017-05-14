package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.dao.exception.DaoException;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.bill.BillDescriptionEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Result set mapper for bill
 */
public class BillMapper implements Mapper<Bill> {

    /**
     * @inheritDoc
     */
    @Override
    public Bill map(ResultSet resultSet) throws SQLException {
        LocalDateTime creationDateTime = resultSet.getTimestamp(BILL_CREATION_DATETIME).toLocalDateTime();

        BillDescriptionEnum description = BillDescriptionEnum
                .of(resultSet.getString(BILL_DESCRIPTION))
                .orElseThrow(() -> new DaoException("Wrong bill description!"));

        return new Bill.Builder()
                .setId(resultSet.getLong(BILL_BILL_ID))
                .setPaid(resultSet.getBoolean(BILL_IS_PAID))
                .setConfirmed(resultSet.getBoolean(BILL_IS_CONFIRMED))
                .setDescription(description)
                .setCashAmount(resultSet.getBigDecimal(BILL_CASH_AMOUNT))
                .setCreationDateTime(creationDateTime)
                .build();
    }
}
