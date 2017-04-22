package org.vitaly.dao.abstraction;

import org.vitaly.model.carModel.CarModel;

import java.util.List;

/**
 * Created by vitaly on 2017-04-22.
 */
public interface CarModelDao extends AbstractDao<CarModel> {
    List<CarModel> findCarsWithPhotos();
}
