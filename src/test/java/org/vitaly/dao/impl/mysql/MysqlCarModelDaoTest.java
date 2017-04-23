package org.vitaly.dao.impl.mysql;

import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Test;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.carModel.CarModel;

import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 2017-04-22.
 */
public class MysqlCarModelDaoTest {
    private static PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();
    private static CarModelDao carModelDao = new MysqlCarModelDao(connection);

    private CarModel carModel1 = TestData.getInstance().getCarModel("carModel1");
    private CarModel carModel2 = TestData.getInstance().getCarModel("carModel2");

    @After
    public void tearDown() throws Exception {
        connection.rollback();
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
    public void findIdOfExistingCarModelReturnsId() throws Exception {
        CarModel createdCarModel = TestUtil.createEntityWithId(carModel1, carModelDao);

        long foundId = carModelDao.findIdOfEntity(carModel1).orElseThrow(AssertionFailedError::new);

        assertThat(createdCarModel, hasId(foundId));
    }

    @Test
    public void findIdNonExistingCarModelReturnsEmptyOptional() throws Exception {
        boolean findResult = carModelDao.findIdOfEntity(carModel1).isPresent();

        assertFalse(findResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIdOfNullShouldThrowException() throws Exception {
        carModelDao.findIdOfEntity(null);
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
    public void createdCarModelHasNullPhotoUrl() throws Exception {
        long createdId = carModelDao.create(carModel1).orElseThrow(AssertionFailedError::new);

        CarModel createdCarModel = carModelDao.findById(createdId).orElseThrow(AssertionFailedError::new);

        assertNull(createdCarModel.getPhotoUrl());
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
    public void findCarModelsWithPhotosReturnsMatchingCars() throws Exception {
        carModelDao.create(carModel1);
        CarModel carModelWithId2 = TestUtil.createEntityWithId(carModel2, carModelDao);

        carModelDao.update(carModelWithId2.getId(), carModel2);

        List<CarModel> carModelList = carModelDao.findCarsWithPhotos();

        assertThat(carModelList, allOf(
                hasItem(carModel2),
                not(hasItem(carModel1))));
    }

    @Test
    public void findCarModelsWithPhotosReturnsEmptyListOnEmptyTable() throws Exception {
        List<CarModel> carList = carModelDao.findCarsWithPhotos();

        assertThat(carList, empty());
    }

    @Test
    public void findCarModelsWithPhotosReturnsEmptyListOnZeroMatches() throws Exception {
        carModelDao.create(carModel1);
        carModelDao.create(carModel2);

        List<CarModel> carList = carModelDao.findCarsWithPhotos();

        assertThat(carList, empty());
    }
}