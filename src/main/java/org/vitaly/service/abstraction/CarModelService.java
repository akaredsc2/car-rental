package org.vitaly.service.abstraction;

import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.impl.dto.CarModelDto;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by vitaly on 2017-04-22.
 */
public interface CarModelService {
    boolean addCarModel(CarModelDto carModelDto);

    List<CarModelDto> getAllMatchingCarModels(Predicate<CarModel> carModelPredicate);

    void updateCarModel(CarModelDto carModelDto);

    List<CarModelDto> findCarsWithPhotos();
}
