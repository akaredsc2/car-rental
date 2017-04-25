package org.vitaly.data;

import org.vitaly.dao.abstraction.AbstractDao;
import org.vitaly.service.impl.dtoMapper.DtoMapper;

import java.util.function.BiConsumer;

/**
 * Created by vitaly on 2017-04-08.
 */
public class TestUtil {
    private TestUtil() {
    }

    public static <T> T createEntityWithId(T entity, AbstractDao<T> dao) {
        long createdId = dao.create(entity).orElseThrow(AssertionError::new);
        return dao.findById(createdId).orElseThrow(AssertionError::new);
    }

    public static <E, D, V> E setEntityAttribute(E entity, BiConsumer<D, V> setter, V value, DtoMapper<E, D> dtoMapper) {
        D dto = dtoMapper.mapEntityToDto(entity);
        setter.accept(dto, value);
        return dtoMapper.mapDtoToEntity(dto);
    }
}
