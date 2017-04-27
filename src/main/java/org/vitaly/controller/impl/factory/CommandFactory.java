package org.vitaly.controller.factory;

/**
 * Created by vitaly on 2017-04-27.
 */
public class CommandFactory {
    private static CommandFactory instance = new CommandFactory();

    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        return instance;
    }
}
