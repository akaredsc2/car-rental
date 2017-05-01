package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.vitaly.util.constants.RequestAttributes.ATTR_MODEL_LIST;

/**
 * Created by vitaly on 01.05.17.
 */
public class GetModelsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO: 01.05.17 extract here and at prepare to add car command
        List<CarModelDto> carModelDtoList = ServiceFactory.getInstance()
                .getCarModelService()
                .getAll();

        request.setAttribute(ATTR_MODEL_LIST, carModelDtoList);

        request.getServletContext()
                .getRequestDispatcher("/pages/catalog/models.jsp")
                .forward(request, response);
    }
}
