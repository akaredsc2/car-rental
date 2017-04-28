package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.command.SignInCommand;
import org.vitaly.controller.impl.command.RegistrationCommand;
import org.vitaly.controller.impl.command.SignOutCommand;
import org.vitaly.controller.impl.command.WrongCommand;

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

        WRONG_COMMAND = new WrongCommand();
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command getCommand(String command) {
        return commandMap.getOrDefault(command, WRONG_COMMAND);
    }
}
