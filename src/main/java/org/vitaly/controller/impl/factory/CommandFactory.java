package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.command.*;
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
        commandMap.put(UrlHttpMethodPair.SIGN_IN, new SignInCommand());
        commandMap.put(UrlHttpMethodPair.SIGN_OUT, new SignOutCommand());
        commandMap.put(UrlHttpMethodPair.REGISTRATION, new RegistrationCommand());

        commandMap.put(UrlHttpMethodPair.CHANGE_LOCALE, new ChangeLocaleCommand());

        commandMap.put(UrlHttpMethodPair.ADD_MODEL_POST, new AddCarModelCommand());
        commandMap.put(UrlHttpMethodPair.ADD_CAR_GET, new PrepareToAddCarCommand());
        commandMap.put(UrlHttpMethodPair.ADD_CAR_POST, new AddCarCommand());
        commandMap.put(UrlHttpMethodPair.ADD_LOCATION_POST, new AddLocationCommand());

        commandMap.put(UrlHttpMethodPair.LOCATIONS_GET, new GetLocationsCommand());
        commandMap.put(UrlHttpMethodPair.MODELS_GET, new GetModelsCommand());
        commandMap.put(UrlHttpMethodPair.CARS_GET, new GetCarsCommand());

        WRONG_COMMAND = new WrongCommand();
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command getCommand(UrlHttpMethodPair urlHttpMethodPair) {
        return commandMap.getOrDefault(urlHttpMethodPair, WRONG_COMMAND);
    }
}
