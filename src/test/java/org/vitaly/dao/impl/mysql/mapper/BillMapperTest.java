package org.vitaly.dao.impl.mysql.mapper;

import org.junit.Test;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.bill.BillDescriptionEnum;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-15.
 */
public class BillMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<Bill> mapper = new BillMapper();

    @Test
    public void mapCorrectlySetsBillParameters() throws Exception {
        Bill expectedBill = new Bill.Builder()
                .setId(20)
                .setPaid(false)
                .setDescription(BillDescriptionEnum.SERVICE)
                .setCashAmount(BigDecimal.valueOf(2222))
                .setCreationDateTime(LocalDateTime.now())
                .build();

        when(resultSet.getLong(BILL_BILL_ID)).thenReturn(expectedBill.getId());
        when(resultSet.getBoolean(BILL_IS_PAID)).thenReturn(expectedBill.isPaid());
        when(resultSet.getString(BILL_DESCRIPTION)).thenReturn(expectedBill.getDescription().toString());
        when(resultSet.getBigDecimal(BILL_CASH_AMOUNT)).thenReturn(expectedBill.getCashAmount());
        when(resultSet.getTimestamp(BILL_CREATION_DATETIME))
                .thenReturn(Timestamp.valueOf(expectedBill.getCreationDateTime()));

        Bill actualBill = mapper.map(resultSet);

        assertThat(actualBill, allOf(equalTo(expectedBill), hasId(expectedBill.getId())));
    }
}