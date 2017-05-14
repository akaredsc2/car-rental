package org.vitaly.dao.impl.mysql.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps result set to entity
 */
@FunctionalInterface
public interface Mapper<T> {

    /**
     * Map result set to entity
     *
     * @param resultSet result set
     * @return entity
     * @throws SQLException if accessing result set failed
     */
    T map(ResultSet resultSet) throws SQLException;
}
