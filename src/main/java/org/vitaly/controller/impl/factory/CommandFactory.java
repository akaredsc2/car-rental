package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.command.*;

import java.util.HashMap;

/**
 * Created by vitaly on 2017-04-27.
 */
public class CommandFactory {
    private static CommandFactory instance = new CommandFactory();

    private final HashMap<String, Command> commandMap;
    private final Command WRONG_COMMAND;

    private CommandFactory() {
        commandMap = new HashMap<>();
        commandMap.put("sign_in", new SignInCommand());
        commandMap.put("sign_out", new SignOutCommand());
        commandMap.put("registration", new RegistrationCommand());
        commandMap.put("add_car_model", new AddCarModelCommand());
        commandMap.put("add_location", new AddLocationCommand());
        commandMap.put("prepare_add_car", new PrepareToAddCarCommand());
        commandMap.put("add_car", new AddCarCommand());

        WRONG_COMMAND = new WrongCommand();
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command getCommand(String command) {
        return commandMap.getOrDefault(command, WRONG_COMMAND);
    }
}
