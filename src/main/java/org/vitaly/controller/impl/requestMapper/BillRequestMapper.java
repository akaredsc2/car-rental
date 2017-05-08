package org.vitaly.controller.impl.requestMapper;

import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;

/**
 * Created by vitaly on 08.05.17.
 */
public class BillRequestMapper implements RequestMapper<BillDto> {
    @Override
    public BillDto map(HttpServletRequest request) {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        long id = PropertyUtils.getLongFromRequest(request, properties, PARAM_BILL_ID);
        BigDecimal cashAmount = PropertyUtils.getBigDecimalFromRequest(request, properties, PARAM_BILL_AMOUNT);

        BillDto billDto = new BillDto();
        billDto.setId(id);
        billDto.setCashAmount(cashAmount);

        return billDto;
    }
}
