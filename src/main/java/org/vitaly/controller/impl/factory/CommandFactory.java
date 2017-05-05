package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.command.*;
import org.vitaly.controller.impl.command.reservation.CreateReservationCommand;
import org.vitaly.controller.impl.command.reservation.GetReservationsCommand;
import org.vitaly.security.UrlHttpMethodPair;

import java.util.HashMap;

/**
 * Created by vitaly on 2017-04-27.
 */
public class CommandFactory {
    private static CommandFactory instance = new CommandFactory();

    private final HashMap<UrlHttpMethodPair, Command> commandMap;
    private final Command WRONG_COMMAND;

    private CommandFactory() {
        commandMap = new HashMap<>();
        commandMap.put(UrlHttpMethodPair.SIGN_IN_POST, new SignInCommand());
        commandMap.put(UrlHttpMethodPair.SIGN_OUT_POST, new SignOutCommand());
        commandMap.put(UrlHttpMethodPair.REGISTRATION_POST, new RegistrationCommand());
        commandMap.put(UrlHttpMethodPair.CHANGE_PASSWORD_POST, new ChangePasswordCommand());

        commandMap.put(UrlHttpMethodPair.CHANGE_LOCALE, new ChangeLocaleCommand());

        commandMap.put(UrlHttpMethodPair.ADD_MODEL_POST, new AddCarModelCommand());
        commandMap.put(UrlHttpMethodPair.ADD_CAR_GET, new PrepareToAddCarCommand());
        commandMap.put(UrlHttpMethodPair.ADD_CAR_POST, new AddCarCommand());
        commandMap.put(UrlHttpMethodPair.ADD_LOCATION_POST, new AddLocationCommand());
        commandMap.put(UrlHttpMethodPair.MOVE_CAR_GET, new PrepareToMoveCarCommand());
        commandMap.put(UrlHttpMethodPair.MOVE_CAR_POST, new MoveCarCommand());
        commandMap.put(UrlHttpMethodPair.CHANGE_CAR_STATE_POST, new ChangeCarStateCommand());
        commandMap.put(UrlHttpMethodPair.UPDATE_CAR_POST, new UpdateCarCommand());
        commandMap.put(UrlHttpMethodPair.PROMOTE_GET, new PrepareToPromoteCommand());
        commandMap.put(UrlHttpMethodPair.PROMOTE_POST, new PromoteCommand());
        commandMap.put(UrlHttpMethodPair.CREATE_RESERVATION_POST, new CreateReservationCommand());
        commandMap.put(UrlHttpMethodPair.ASSIGN_POST, new AssignAdminToReservationCommand());
        commandMap.put(UrlHttpMethodPair.CHANGE_RESERVATION_STATE_POST, new ChangeReservationStateCommand());

        commandMap.put(UrlHttpMethodPair.LOCATIONS_GET, new GetLocationsCommand());
        commandMap.put(UrlHttpMethodPair.MODELS_GET, new GetModelsCommand());
        commandMap.put(UrlHttpMethodPair.CARS_GET, new GetCarsCommand());
        commandMap.put(UrlHttpMethodPair.RESERVATIONS_GET, new GetReservationsCommand());

        WRONG_COMMAND = new WrongCommand();
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command getCommand(UrlHttpMethodPair urlHttpMethodPair) {
        return commandMap.getOrDefault(urlHttpMethodPair, WRONG_COMMAND);
    }
}
