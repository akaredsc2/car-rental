package org.vitaly.controller.impl.factory;

import org.junit.Test;
import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.command.car.AddCarCommand;
import org.vitaly.controller.impl.command.WrongCommand;
import org.vitaly.security.UrlHttpMethodPair;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-30.
 */
public class CommandFactoryTest {

    @Test
    public void getExistingCommandReturnsThatCommand() throws Exception {
        UrlHttpMethodPair existingUrl = UrlHttpMethodPair.ADD_CAR_POST;

        Command actualCommand = CommandFactory.getInstance().getCommand(existingUrl);

        assertThat(actualCommand, instanceOf(AddCarCommand.class));
    }

    @Test
    public void getNonExistingCommandReturnsWrongCommand() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String command = "someCommand";

        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter(eq("command"))).thenReturn(command);
        UrlHttpMethodPair nonExistingUrl = UrlHttpMethodPair.fromRequest(request);
        Command actualCommand = CommandFactory.getInstance().getCommand(nonExistingUrl);

        assertThat(actualCommand, instanceOf(WrongCommand.class));
    }
}