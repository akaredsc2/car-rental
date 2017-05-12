package org.vitaly.controller.impl.command.location;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.CommandUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_UPDATE_PHOTO;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.RequestParameters.PARAM_LOCATION_PHOTO;

/**
 * Created by vitaly on 2017-05-12.
 */
public class UpdateLocationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocationDto locationDto = RequestMapperFactory.getInstance()
                .getLocationRequestMapper()
                .map(request);

        CommandUtil.uploadImage(request, PARAM_LOCATION_PHOTO, locationDto::setPhotoUrl);

        boolean isLocationPhotoUpdated = ServiceFactory.getInstance()
                .getLocationService()
                .updateLocation(locationDto);

        if (isLocationPhotoUpdated) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_UPDATE_PHOTO);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
