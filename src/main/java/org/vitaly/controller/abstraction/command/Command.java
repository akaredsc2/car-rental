package org.vitaly.controller.impl.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by vitaly on 2017-04-27.
 */
public interface Command {
    void execute(HttpServletRequest request, HttpServletResponse response);
}
