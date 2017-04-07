package org.vitaly.util.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.util.dao.mapper.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by vitaly on 2017-04-07.
 */
public class DaoTemplate {
    private static final Logger logger = LogManager.getLogger(DaoTemplate.class.getName());
    private PooledConnection connection;

    public DaoTemplate(PooledConnection connection) {
        this.connection = connection;
    }

    public <T> T executeSelect(String query, Mapper<T> mapper, Map<Integer, Object> parameterMap) {
        T result = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Integer i : parameterMap.keySet()) {
                statement.setObject(i, parameterMap.get(i));
            }
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null) {
                result = processResultSet(resultSet, mapper);
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.error("Error while executing select query.", e);
        }

        return result;
    }

    private <T> T processResultSet(ResultSet resultSet, Mapper<T> mapper) throws SQLException {
        if (resultSet.next()) {
            return mapper.map(resultSet);
        }
        return null;
    }
}
