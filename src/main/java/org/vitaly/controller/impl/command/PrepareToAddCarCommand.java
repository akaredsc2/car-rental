package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.vitaly.util.PropertyNames.ATTR_ALL_MODEL_LIST;

/**
 * Created by vitaly on 29.04.17.
 */
public class PrepareToAddCarCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CarModelDto> carModels = ServiceFactory.getInstance()
                .getCarModelService()
                .getAllCarModels();

        // TODO: 30.04.17 empty list case
        if (!carModels.isEmpty()) {
            request.setAttribute(ATTR_ALL_MODEL_LIST, carModels);
        }

        request.getServletContext()
                .getRequestDispatcher("/add_car.jsp")
                .forward(request, response);
    }
}
