package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.bill.Bill;
import org.vitaly.service.impl.dto.BillDto;

/**
 * Created by vitaly on 23.04.17.
 */
public class BillDtoMapper implements DtoMapper<Bill, BillDto> {

    @Override
    public Bill mapDtoToEntity(BillDto dto) {
        return new Bill.Builder()
                .setId(dto.getId())
                .setDescription(dto.getDescription())
                .setCashAmount(dto.getCashAmount())
                .setCreationDateTime(dto.getCreationDateTime())
                .setPaid(dto.isPaid())
                .build();
    }

    @Override
    public BillDto mapEntityToDto(Bill entity) {
        BillDto billDto = new BillDto();

        billDto.setId(entity.getId());
        billDto.setDescription(entity.getDescription());
        billDto.setCashAmount(entity.getCashAmount());
        billDto.setCreationDateTime(entity.getCreationDateTime());
        billDto.setPaid(entity.isPaid());

        return billDto;
    }
}
