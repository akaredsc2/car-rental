package org.vitaly.service.abstraction;

import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-22.
 */
public interface CarModelService {
    boolean addCarModel(CarModelDto carModelDto);

    List<CarModelDto> getAll();

    boolean updateCarModel(CarModelDto carModelDto);

    Optional<CarModelDto> findModelOfCar(CarDto carDto);
}
