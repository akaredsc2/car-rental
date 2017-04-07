package org.vitaly.util.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-07.
 */
@FunctionalInterface
public interface Mapper<T> {
    T map(ResultSet resultSet) throws SQLException;
}
