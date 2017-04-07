package org.vitaly.util.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.util.dao.mapper.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public <T> List<T> executeSelect(String query, Mapper<T> mapper, Map<Integer, Object> parameterMap) {
        List<T> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Integer i : parameterMap.keySet()) {
                statement.setObject(i, parameterMap.get(i));
            }

            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    T entry = mapper.map(resultSet);
                    result.add(entry);
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.error("Error while executing select query.", e);
        }

        return result;
    }

    public <T> T executeSelectOne(String query, Mapper<T> mapper, Map<Integer, Object> parameterMap) {
        List<T> result = executeSelect(query, mapper, parameterMap);

        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
