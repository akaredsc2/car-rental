package org.vitaly.util;

import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

import static org.vitaly.util.constants.RequestParameters.PARAMETERS;

/**
 * Created by vitaly on 2017-05-12.
 */
public class CommandUtil {
    private CommandUtil() {
    }

    public static void uploadImage(HttpServletRequest request, String parameterName, Consumer<String> urlConsumer)
            throws IOException, ServletException {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        Part filePart = request.getPart(properties.getProperty(parameterName));

        if (filePart.getSize() != 0) {
            filePart.write(filePart.getSubmittedFileName());

            File tempDir = (File) request.getServletContext().getAttribute(ServletContext.TEMPDIR);
            File file = new File(tempDir.getAbsolutePath() + File.separator + filePart.getSubmittedFileName());
            file.deleteOnExit();

            String uploadedPhotoUrl = ServiceFactory.getInstance()
                    .getImageService()
                    .upload(file);

            urlConsumer.accept(uploadedPhotoUrl);
        }
    }
}
