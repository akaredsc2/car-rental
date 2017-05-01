package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.vitaly.util.constants.RequestAttributes.ATTR_ALL_LOCATION_LIST;

/**
 * Created by vitaly on 01.05.17.
 */
public class ShowLocationsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<LocationDto> locationDtoList = ServiceFactory.getInstance()
                .getLocationService()
                .getAll();

        request.setAttribute(ATTR_ALL_LOCATION_LIST, locationDtoList);

        request.getServletContext().getRequestDispatcher("/pages/catalog/locations.jsp").forward(request, response);
    }
}
