package org.vitaly.controller.impl.requestMapper;

import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;

/**
 * Created by vitaly on 2017-05-05.
 */
public class ReservationRequestMapper implements RequestMapper<ReservationDto> {

    @Override
    public ReservationDto map(HttpServletRequest request) {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        long id = PropertyUtils.getLongFromRequest(request, properties, PARAM_RESERVATION_ID);

        // TODO: 2017-05-05 consider replacing with car request mapper
        long carId = PropertyUtils.getLongFromRequest(request, properties, PARAM_RESERVATION_CAR);
        CarDto carDto = new CarDto();
        carDto.setId(carId);

        LocalDateTime pickUpDatetime =
                PropertyUtils.getLocalDateTimeFromRequest(request, properties, PARAM_RESERVATION_PICK);
        LocalDateTime dropOffDatetime =
                PropertyUtils.getLocalDateTimeFromRequest(request, properties, PARAM_RESERVATION_DROP);

        String reservationStateString = request.getParameter(properties.getProperty(PARAM_RESERVATION_STATE));
        ReservationState reservationState = ReservationStateEnum
                .stateOf(reservationStateString)

                // TODO: 2017-05-05 consider new state instead of null. same for car states
                .orElse(null);

        String rejectionReason = request.getParameter(properties.getProperty(PARAM_RESERVATION_REASON));

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(id);
        reservationDto.setCar(carDto);
        reservationDto.setPickUpDatetime(pickUpDatetime);
        reservationDto.setDropOffDatetime(dropOffDatetime);
        reservationDto.setState(reservationState);
        reservationDto.setRejectionReason(rejectionReason);
        // TODO: 2017-05-05 drop off location

        return reservationDto;
    }
}
