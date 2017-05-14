package org.vitaly.dao.impl.mysql;

import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.data.TestUtil;
import org.vitaly.model.carModel.CarModel;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 2017-04-22.
 */
public class MysqlCarModelDaoTest {
    private CarModelDao carModelDao = new MysqlCarModelDao();

    private CarModel carModel1 = new CarModel.Builder()
            .setName("Ford Focus")
            .setPhotoUrl("none")
            .setSeatCount(5)
            .setDoorCount(4)
            .setHorsePowerCount(150)
            .build();

    private CarModel carModel2 = new CarModel.Builder()
            .setName("Ford Fiesta")
            .setPhotoUrl("none")
            .setSeatCount(2)
            .setDoorCount(2)
            .setHorsePowerCount(100)
            .build();

    @Before
    public void setUp() throws Exception {
        TransactionManager.startTransaction();
    }

    @After
    public void tearDown() throws Exception {
        TransactionManager.rollback();
    }

    @Test
    public void findByIdExistingCarModelReturnsCarModel() throws Exception {
        long createdId = carModelDao.create(carModel1).orElseThrow(AssertionFailedError::new);

        CarModel foundCarModel = carModelDao.findById(createdId).orElseThrow(AssertionFailedError::new);

        assertEquals(foundCarModel, carModel1);
    }

    @Test
    public void findByIdNonExistingCarModelReturnsEmptyOptional() throws Exception {
        boolean findResult = carModelDao.findById(-1L).isPresent();

        assertFalse(findResult);
    }

    @Test
    public void getAllContainsAllCreatedCarModels() throws Exception {
        carModelDao.create(carModel1);
        carModelDao.create(carModel2);

        List<CarModel> carModelList = carModelDao.getAll();

        assertThat(carModelList, hasItems(carModel1, carModel2));

    }

    @Test
    public void getAllOnEmptyTableReturnsEmptyList() throws Exception {
        List<CarModel> carList = carModelDao.getAll();

        assertThat(carList, empty());
    }

    @Test
    public void successfulCreationReturnsId() throws Exception {
        boolean creationResult = carModelDao.create(carModel1).isPresent();

        assertTrue(creationResult);
    }

    @Test
    public void creatingCarModelWithSameNameReturnsEmptyOptional() throws Exception {
        carModelDao.create(carModel1);
        boolean creationResult = carModelDao.create(carModel1).isPresent();

        assertFalse(creationResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullShouldThrowException() throws Exception {
        carModelDao.create(null);
    }

    @Test
    public void updateExistingCarModelReturnsOne() throws Exception {
        CarModel createdCarModel = TestUtil.createEntityWithId(carModel1, carModelDao);

        int updateCount = carModelDao.update(createdCarModel.getId(), carModel2);

        assertEquals(updateCount, 1);
    }

    @Test
    public void updateExistingCarModelUpdatesCarModelInDatabase() throws Exception {
        String updatedPhotoUrl = "photo url";
        CarModel carModelWithUpdate = new CarModel.Builder()
                .setPhotoUrl(updatedPhotoUrl)
                .setName(carModel1.getName())
                .setDoorCount(carModel1.getDoorCount())
                .setSeatCount(carModel1.getSeatCount())
                .setHorsePowerCount(carModel1.getHorsePowerCount())
                .build();

        CarModel createdCarModel = TestUtil.createEntityWithId(carModel1, carModelDao);
        long createdCarModelId = createdCarModel.getId();
        carModelDao.update(createdCarModelId, carModelWithUpdate);

        CarModel updatedCarModel = carModelDao.findById(createdCarModelId).orElseThrow(AssertionFailedError::new);

        assertEquals(updatedCarModel.getPhotoUrl(), updatedPhotoUrl);
    }

    @Test
    public void updateNonExistingCarModelReturnsZero() throws Exception {
        int updateCount = carModelDao.update(-1, carModel2);

        assertEquals(updateCount, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullCarModelShouldThrowException() throws Exception {
        carModelDao.update(1, null);
    }

    @Test
    public void findModelOfExistingCarReturnsModel() throws Exception {
        long carId = 1;
        long modelId = carModelDao.create(carModel1).orElseThrow(AssertionFailedError::new);

        DaoTemplate.executeInsert(
                "INSERT INTO " +
                        "car(car_id, car_status, model_id, registration_plate, color, price_per_day) " +
                        "VALUES (1, 'available', ?, '', '', 1)", Collections.singletonMap(1, modelId));
        CarModel carModel = carModelDao.findModelOfCar(carId).orElseThrow(AssertionFailedError::new);

        assertThat(carModel, hasId(modelId));
    }

    @Test
    public void findModelOfNonExistingCarReturnsEmptyOptional() throws Exception {
        boolean isModelFound = carModelDao.findModelOfCar(-1).isPresent();

        assertFalse(isModelFound);
    }
}