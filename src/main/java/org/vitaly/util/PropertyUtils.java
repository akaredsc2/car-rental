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
 * Created by vitaly on 28.04.17.
 */
public class PropertyUtils {
    private static Logger logger = LogManager.getLogger(PropertyUtils.class.getName());

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

    public static long getLongFromRequest(HttpServletRequest request,
                                          Properties properties, String parameterName) {
        String idString = request.getParameter(properties.getProperty(parameterName));
        return (idString == null) ? 0 : Long.valueOf(idString);
    }

    public static int getIntFromRequest(HttpServletRequest request,
                                        Properties properties, String parameterName) {
        String idString = request.getParameter(properties.getProperty(parameterName));
        return (idString == null) ? 0 : Integer.valueOf(idString);
    }

    public static LocalDate getLocalDateFromRequest(HttpServletRequest request,
                                                    Properties properties, String parameterName) {
        String birthDateString = request.getParameter(properties.getProperty(parameterName));
        return (birthDateString == null) ?
                null :
                LocalDateTime.parse(birthDateString, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
    }

    public static BigDecimal getBigDecimalFromRequest(HttpServletRequest request,
                                                      Properties properties, String parameterName) {
        String bigDecimalString = request.getParameter(properties.getProperty(parameterName));
        return (bigDecimalString == null) ? null : new BigDecimal(bigDecimalString);
    }
}
