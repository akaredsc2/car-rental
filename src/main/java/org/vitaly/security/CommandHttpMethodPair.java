package org.vitaly.security;

import org.vitaly.util.InputChecker;

import javax.servlet.http.HttpServletRequest;

/**
 * Pair of command name and http method
 */
public class CommandHttpMethodPair {
    public static final CommandHttpMethodPair SIGN_IN_POST = new CommandHttpMethodPair("sign_in", HttpMethod.POST);
    public static final CommandHttpMethodPair SIGN_OUT_POST = new CommandHttpMethodPair("sign_out", HttpMethod.POST);
    public static final CommandHttpMethodPair REGISTRATION_POST =
            new CommandHttpMethodPair("registration", HttpMethod.POST);
    public static final CommandHttpMethodPair CHANGE_PASSWORD_POST =
            new CommandHttpMethodPair("change_password", HttpMethod.POST);

    public static final CommandHttpMethodPair CHANGE_LOCALE = new CommandHttpMethodPair("locale", HttpMethod.GET);

    public static final CommandHttpMethodPair ADD_MODEL_POST = new CommandHttpMethodPair("add_model", HttpMethod.POST);
    public static final CommandHttpMethodPair ADD_CAR_GET = new CommandHttpMethodPair("add_car", HttpMethod.GET);
    public static final CommandHttpMethodPair ADD_CAR_POST = new CommandHttpMethodPair("add_car", HttpMethod.POST);
    public static final CommandHttpMethodPair ADD_LOCATION_POST =
            new CommandHttpMethodPair("add_location", HttpMethod.POST);
    public static final CommandHttpMethodPair MOVE_CAR_GET = new CommandHttpMethodPair("move_car", HttpMethod.GET);
    public static final CommandHttpMethodPair MOVE_CAR_POST = new CommandHttpMethodPair("move_car", HttpMethod.POST);
    public static final CommandHttpMethodPair CHANGE_CAR_STATE_POST =
            new CommandHttpMethodPair("change_car_state", HttpMethod.POST);
    public static final CommandHttpMethodPair UPDATE_CAR_POST =
            new CommandHttpMethodPair("update_car", HttpMethod.POST);
    public static final CommandHttpMethodPair PROMOTE_GET = new CommandHttpMethodPair("promote", HttpMethod.GET);
    public static final CommandHttpMethodPair PROMOTE_POST = new CommandHttpMethodPair("promote", HttpMethod.POST);
    public static final CommandHttpMethodPair CREATE_RESERVATION_POST =
            new CommandHttpMethodPair("create_reservation", HttpMethod.POST);
    public static final CommandHttpMethodPair ASSIGN_POST = new CommandHttpMethodPair("assign", HttpMethod.POST);
    public static final CommandHttpMethodPair CHANGE_RESERVATION_STATE_POST =
            new CommandHttpMethodPair("change_reservation_state", HttpMethod.POST);
    public static final CommandHttpMethodPair CANCEL_RESERVATION_POST =
            new CommandHttpMethodPair("cancel_reservation", HttpMethod.POST);
    public static final CommandHttpMethodPair PAY_POST = new CommandHttpMethodPair("pay", HttpMethod.POST);
    public static final CommandHttpMethodPair CONFIRM_POST = new CommandHttpMethodPair("confirm", HttpMethod.POST);
    public static final CommandHttpMethodPair ADD_DAMAGE_BILL_POST =
            new CommandHttpMethodPair("add_damage_bill", HttpMethod.POST);
    public static final CommandHttpMethodPair UPDATE_LOCATION_POST =
            new CommandHttpMethodPair("update_location", HttpMethod.POST);
    public static final CommandHttpMethodPair UPDATE_MODEL_POST =
            new CommandHttpMethodPair("update_model", HttpMethod.POST);

    public static final CommandHttpMethodPair LOCATIONS_GET = new CommandHttpMethodPair("locations", HttpMethod.GET);
    public static final CommandHttpMethodPair MODELS_GET = new CommandHttpMethodPair("models", HttpMethod.GET);
    public static final CommandHttpMethodPair CARS_GET = new CommandHttpMethodPair("cars", HttpMethod.GET);
    public static final CommandHttpMethodPair RESERVATIONS_GET =
            new CommandHttpMethodPair("reservations", HttpMethod.GET);
    public static final CommandHttpMethodPair BILLS_GET = new CommandHttpMethodPair("bills", HttpMethod.GET);

    private String command;
    private HttpMethod httpMethod;

    CommandHttpMethodPair(String command, HttpMethod httpMethod) {
        this.command = command;
        this.httpMethod = httpMethod;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommandHttpMethodPair that = (CommandHttpMethodPair) o;

        return command.equals(that.command) && httpMethod == that.httpMethod;
    }

    @Override
    public int hashCode() {
        int result = command.hashCode();
        result = 31 * result + httpMethod.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CommandHttpMethodPair{" +
                "command='" + command + '\'' +
                ", httpMethod=" + httpMethod +
                '}';
    }

    /**
     * Static factory method to create instance from request
     *
     * @param request http servlet request
     * @return created pair
     */
    public static CommandHttpMethodPair fromRequest(HttpServletRequest request) {
        InputChecker.requireNotNull(request, "Request must not be null!");

        String command = request.getParameter("command");
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        return new CommandHttpMethodPair(command, httpMethod);
    }
}
