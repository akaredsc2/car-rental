package org.vitaly.controller.impl.factory;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.command.*;
import org.vitaly.controller.impl.command.bills.AddDamageBillCommand;
import org.vitaly.controller.impl.command.bills.ConfirmPaymentCommand;
import org.vitaly.controller.impl.command.bills.GetBillsCommand;
import org.vitaly.controller.impl.command.bills.PayCommand;
import org.vitaly.controller.impl.command.car.*;
import org.vitaly.controller.impl.command.carModel.AddCarModelCommand;
import org.vitaly.controller.impl.command.carModel.GetModelsCommand;
import org.vitaly.controller.impl.command.carModel.UpdateCarModelCommand;
import org.vitaly.controller.impl.command.location.AddLocationCommand;
import org.vitaly.controller.impl.command.location.GetLocationsCommand;
import org.vitaly.controller.impl.command.location.UpdateLocationCommand;
import org.vitaly.controller.impl.command.reservation.*;
import org.vitaly.controller.impl.command.user.*;
import org.vitaly.security.CommandHttpMethodPair;

import java.util.HashMap;

/**
 * Created by vitaly on 2017-04-27.
 */
public class CommandFactory {
    private static CommandFactory instance = new CommandFactory();

    private final HashMap<CommandHttpMethodPair, Command> commandMap;
    private final Command WRONG_COMMAND;

    private CommandFactory() {
        commandMap = new HashMap<>();
        commandMap.put(CommandHttpMethodPair.SIGN_IN_POST, new SignInCommand());
        commandMap.put(CommandHttpMethodPair.SIGN_OUT_POST, new SignOutCommand());
        commandMap.put(CommandHttpMethodPair.REGISTRATION_POST, new RegistrationCommand());
        commandMap.put(CommandHttpMethodPair.CHANGE_PASSWORD_POST, new ChangePasswordCommand());

        commandMap.put(CommandHttpMethodPair.CHANGE_LOCALE, new ChangeLocaleCommand());

        commandMap.put(CommandHttpMethodPair.ADD_MODEL_POST, new AddCarModelCommand());
        commandMap.put(CommandHttpMethodPair.ADD_CAR_GET, new PrepareToAddCarCommand());
        commandMap.put(CommandHttpMethodPair.ADD_CAR_POST, new AddCarCommand());
        commandMap.put(CommandHttpMethodPair.ADD_LOCATION_POST, new AddLocationCommand());
        commandMap.put(CommandHttpMethodPair.MOVE_CAR_GET, new PrepareToMoveCarCommand());
        commandMap.put(CommandHttpMethodPair.MOVE_CAR_POST, new MoveCarCommand());
        commandMap.put(CommandHttpMethodPair.CHANGE_CAR_STATE_POST, new ChangeCarStateCommand());
        commandMap.put(CommandHttpMethodPair.UPDATE_CAR_POST, new UpdateCarCommand());
        commandMap.put(CommandHttpMethodPair.PROMOTE_GET, new PrepareToPromoteCommand());
        commandMap.put(CommandHttpMethodPair.PROMOTE_POST, new PromoteCommand());
        commandMap.put(CommandHttpMethodPair.CREATE_RESERVATION_POST, new CreateReservationCommand());
        commandMap.put(CommandHttpMethodPair.ASSIGN_POST, new AssignAdminToReservationCommand());
        commandMap.put(CommandHttpMethodPair.CHANGE_RESERVATION_STATE_POST, new ChangeReservationStateCommand());
        commandMap.put(CommandHttpMethodPair.CANCEL_RESERVATION_POST, new CancelReservationCommand());
        commandMap.put(CommandHttpMethodPair.PAY_POST, new PayCommand());
        commandMap.put(CommandHttpMethodPair.CONFIRM_POST, new ConfirmPaymentCommand());
        commandMap.put(CommandHttpMethodPair.ADD_DAMAGE_BILL_POST, new AddDamageBillCommand());
        commandMap.put(CommandHttpMethodPair.UPDATE_LOCATION_POST, new UpdateLocationCommand());
        commandMap.put(CommandHttpMethodPair.UPDATE_MODEL_POST, new UpdateCarModelCommand());

        commandMap.put(CommandHttpMethodPair.LOCATIONS_GET, new GetLocationsCommand());
        commandMap.put(CommandHttpMethodPair.MODELS_GET, new GetModelsCommand());
        commandMap.put(CommandHttpMethodPair.CARS_GET, new GetCarsCommand());
        commandMap.put(CommandHttpMethodPair.RESERVATIONS_GET, new GetReservationsCommand());
        commandMap.put(CommandHttpMethodPair.BILLS_GET, new GetBillsCommand());

        WRONG_COMMAND = new WrongCommand();
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command getCommand(CommandHttpMethodPair commandHttpMethodPair) {
        return commandMap.getOrDefault(commandHttpMethodPair, WRONG_COMMAND);
    }
}
