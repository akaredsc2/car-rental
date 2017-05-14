package org.vitaly.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Utility class for dealing with properties
 */
public class PropertyUtils {
    private static Logger logger = LogManager.getLogger(PropertyUtils.class.getName());

    private PropertyUtils() {
    }

    /**
     * Reads properties with supplied name from resources
     *
     * @param name properties to read
     * @return properties
     */
    public static Properties readProperties(String name) {
        InputStream inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream(name);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            logger.error("Error while reading properties", e);
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Read long from request
     *
     * @param request       request
     * @param properties    properties with parameter name
     * @param parameterName parameter name
     * @return number or -1 if no such parameter in request
     */
    public static long getLongFromRequest(HttpServletRequest request,
                                          Properties properties, String parameterName) {
        String idString = request.getParameter(properties.getProperty(parameterName));
        return (idString == null) ? -1 : Long.valueOf(idString);
    }

    /**
     * Read int from request
     *
     * @param request       request
     * @param properties    properties with parameter name
     * @param parameterName parameter name
     * @return number or -1 if no such parameter in request
     */
    public static int getIntFromRequest(HttpServletRequest request,
                                        Properties properties, String parameterName) {
        String idString = request.getParameter(properties.getProperty(parameterName));
        return (idString == null) ? -1 : Integer.valueOf(idString);
    }

    /**
     * Read local date from request
     *
     * @param request       request
     * @param properties    properties with parameter name
     * @param parameterName parameter name
     * @return local date or null if no such parameter in request
     */
    public static LocalDate getLocalDateFromRequest(HttpServletRequest request,
                                                    Properties properties, String parameterName) {
        String localDateString = request.getParameter(properties.getProperty(parameterName));
        return (localDateString == null) ?
                null :
                LocalDate.parse(localDateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Read local date time from request
     *
     * @param request       request
     * @param properties    properties with parameter name
     * @param parameterName parameter name
     * @return local date time or null if no such parameter in request
     */
    public static LocalDateTime getLocalDateTimeFromRequest(HttpServletRequest request,
                                                            Properties properties, String parameterName) {
        String localDateTimeString = request.getParameter(properties.getProperty(parameterName));
        return (localDateTimeString == null) ?
                null :
                LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Read BigDecimal from request
     *
     * @param request       request
     * @param properties    properties with parameter name
     * @param parameterName parameter name
     * @return number or null if no such parameter in request
     */
    public static BigDecimal getBigDecimalFromRequest(HttpServletRequest request,
                                                      Properties properties, String parameterName) {
        String bigDecimalString = request.getParameter(properties.getProperty(parameterName));
        return (bigDecimalString == null) ? null : new BigDecimal(bigDecimalString);
    }
}
