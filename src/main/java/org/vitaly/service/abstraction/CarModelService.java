package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.CarModelDto;

import java.util.List;

/**
 * Created by vitaly on 2017-04-22.
 */
public interface CarModelService {
    boolean addCarModel(CarModelDto carModelDto);

    List<CarModelDto> getAll();

    void updateCarModel(CarModelDto carModelDto);

    List<CarModelDto> findCarsWithPhotos();
}
