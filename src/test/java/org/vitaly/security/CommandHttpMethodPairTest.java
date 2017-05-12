package org.vitaly.security;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-30.
 */
public class CommandHttpMethodPairTest {

    @Test
    public void fromRequestFetchesCommandParameterFromRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String method = "POST";
        String command = "someCommand";

        when(request.getMethod()).thenReturn(method);
        when(request.getParameter(eq("command"))).thenReturn(command);
        CommandHttpMethodPair actual = CommandHttpMethodPair.fromRequest(request);

        CommandHttpMethodPair expected = new CommandHttpMethodPair(command, HttpMethod.valueOf(method));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromNullRequestShouldThrowException() throws Exception {
        CommandHttpMethodPair.fromRequest(null);
    }
}