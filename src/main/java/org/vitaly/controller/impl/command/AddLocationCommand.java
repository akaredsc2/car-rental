package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.PropertyNames.ATTR_ERROR;

/**
 * Created by vitaly on 28.04.17.
 */
public class AddLocationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocationDto locationDto = RequestMapperFactory.getInstance()
                .getLocationRequestMapper()
                .map(request);

        boolean isAdded = ServiceFactory.getInstance()
                .getLocationService()
                .addNewLocation(locationDto);

        if (isAdded) {
            request.getServletContext().getRequestDispatcher("/home.jsp").forward(request, response);
        } else {
            request.setAttribute(ATTR_ERROR, "failed to add location");
            request.getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
