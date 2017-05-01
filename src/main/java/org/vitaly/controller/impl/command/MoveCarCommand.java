package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vitaly on 01.05.17.
 */
public class MoveCarCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

        CarDto carDto = requestMapperFactory
                .getCarRequestMapper()
                .map(request);

        LocationDto locationDto = requestMapperFactory
                .getLocationRequestMapper()
                .map(request);

        ServiceFactory.getInstance()
                .getCarService()
                .moveCarToLocation(carDto, locationDto);

        request.getServletContext().getRequestDispatcher("/home.jsp").forward(request, response);
    }
}
