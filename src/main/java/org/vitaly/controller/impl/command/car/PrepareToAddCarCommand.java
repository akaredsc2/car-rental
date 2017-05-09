package org.vitaly.controller.impl.command.car;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.vitaly.util.constants.Pages.ADD_CAR_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_MODEL_LIST;

/**
 * Created by vitaly on 29.04.17.
 */
public class PrepareToAddCarCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CarModelDto> carModels = ServiceFactory.getInstance()
                .getCarModelService()
                .getAll();

        request.setAttribute(ATTR_MODEL_LIST, carModels);

        request.getServletContext()
                .getRequestDispatcher(ADD_CAR_JSP)
                .forward(request, response);
    }
}
