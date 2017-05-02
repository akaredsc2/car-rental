package org.vitaly.dao.abstraction;

import org.vitaly.model.carModel.CarModel;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-22.
 */
public interface CarModelDao extends AbstractDao<CarModel> {
    List<CarModel> findCarModelsWithPhotos();

    Optional<CarModel> findModelOfCar(long carId);
}
