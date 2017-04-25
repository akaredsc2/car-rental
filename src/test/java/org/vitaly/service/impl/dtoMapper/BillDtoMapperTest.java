package org.vitaly.service.impl.dtoMapper;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.model.bill.Bill;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 23.04.17.
 */
public class BillDtoMapperTest {
    private DtoMapper<Bill, BillDto> mapper = DtoMapperFactory.getInstance().getBillDtoMapper();
    private BillDto expectedBillDto;
    private Bill expectedBill;

    @Before
    public void setUp() throws Exception {
        int id = 1;
        String description = "description";
        BigDecimal cashAmount = BigDecimal.ONE;
        LocalDateTime creationDateTime = LocalDateTime.now();
        boolean isPaid = false;

        expectedBillDto = new BillDto();
        expectedBillDto.setId(id);
        expectedBillDto.setDescription(description);
        expectedBillDto.setCashAmount(cashAmount);
        expectedBillDto.setCreationDateTime(creationDateTime);
        expectedBillDto.setPaid(isPaid);

        expectedBill = new Bill.Builder()
                .setId(id)
                .setDescription(description)
                .setCashAmount(cashAmount)
                .setCreationDateTime(creationDateTime)
                .setPaid(isPaid)
                .build();
    }

    @Test
    public void mapDtoToEntity() throws Exception {
        Bill actualBill = mapper.mapDtoToEntity(expectedBillDto);

        assertThat(actualBill, allOf(
                equalTo(expectedBill),
                hasId(expectedBill.getId())));
    }

    @Test
    public void mapEntityToDto() throws Exception {
        BillDto actualBillDto = mapper.mapEntityToDto(expectedBill);

        assertEquals(expectedBillDto, actualBillDto);
    }
}