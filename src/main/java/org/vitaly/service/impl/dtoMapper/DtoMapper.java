package org.vitaly.service.impl.dtoMapper;

/**
 * Created by vitaly on 23.04.17.
 */
public interface DtoMapper<E, T> {
    E mapDtoToEntity(T dto);

    T mapEntityToDto(E entity);
}
