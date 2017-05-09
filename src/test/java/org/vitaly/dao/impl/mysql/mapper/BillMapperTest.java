package org.vitaly.dao.impl.mysql.mapper;

import org.junit.Test;
import org.vitaly.data.TestData;
import org.vitaly.model.bill.Bill;

import java.sql.ResultSet;
import java.sql.Timestamp;

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
        Bill expectedBill = TestData.getInstance().getBill("bill1");

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