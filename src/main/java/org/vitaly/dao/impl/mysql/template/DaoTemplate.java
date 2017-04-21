package org.vitaly.dao.impl.mysql.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.mapper.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vitaly on 2017-04-07.
 */
public class DaoTemplate {
    private static final String ERROR_WHILE_EXECUTING_SELECT_QUERY = "Error while executing select query.";
    private static final String ERROR_WHILE_EXECUTING_UPDATE_QUERY = "Error while executing update query.";
    private static final String ERROR_WHILE_EXECUTING_INSERT_QUERY = "Error while executing insert query.";
    private static final int ERROR_CODE_DUPLICATE_ENTRY = 1062;

    private static Logger logger = LogManager.getLogger(DaoTemplate.class.getName());
    private PooledConnection connection;

    public DaoTemplate(PooledConnection connection) {
        this.connection = connection;
    }

    public <T> List<T> executeSelect(String query, Mapper<T> mapper, Map<Integer, Object> parameterMap) {
        List<T> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setQueryParameters(statement, parameterMap);

            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    T entry = mapper.map(resultSet);
                    result.add(entry);
                }
                resultSet.close();
            }

            return result;
        } catch (SQLException e) {
            logger.error(ERROR_WHILE_EXECUTING_SELECT_QUERY, e);
            throw new DaoException(ERROR_WHILE_EXECUTING_SELECT_QUERY, e);
        }
    }

    private void setQueryParameters(PreparedStatement statement, Map<Integer, Object> parameterMap)
            throws SQLException {
        for (Integer i : parameterMap.keySet()) {
            statement.setObject(i, parameterMap.get(i));
        }
    }

    public <T> T executeSelectOne(String query, Mapper<T> mapper, Map<Integer, Object> parameterMap) {
        List<T> result = executeSelect(query, mapper, parameterMap);

        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    // TODO: 2017-04-07 add transaction isolation
    public Long executeInsert(String query, Map<Integer, Object> parameterMap) {
        Long insertedId = null;

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setQueryParameters(statement, parameterMap);

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                insertedId = resultSet.getLong(1);
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.error(ERROR_WHILE_EXECUTING_INSERT_QUERY, e);
            if (e.getErrorCode() != ERROR_CODE_DUPLICATE_ENTRY) {
                throw new DaoException(ERROR_WHILE_EXECUTING_INSERT_QUERY, e);
            }
        }

        return insertedId;
    }

    // TODO: 2017-04-07 add transaction isolation
    public int executeUpdate(String query, Map<Integer, Object> parameterMap) {
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setQueryParameters(statement, parameterMap);

            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(ERROR_WHILE_EXECUTING_UPDATE_QUERY, e);
            throw new DaoException(ERROR_WHILE_EXECUTING_UPDATE_QUERY, e);
        }
    }
}
