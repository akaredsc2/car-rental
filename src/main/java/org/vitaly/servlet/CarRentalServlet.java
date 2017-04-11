package org.vitaly.servlet;

import org.vitaly.connectionPool.abstraction.ConnectionPool;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.model.user.User;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.implementation.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-11.
 */
public class CarRentalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");

        if (command != null) {
            if (command.equals("log_in")) {
                String userLogin = req.getParameter("user_login");
                String userPassword = req.getParameter("user_password");

                ConnectionPool pool = MysqlConnectionPool.getInstance();
                PooledConnection connection = pool.getConnection();
                UserService userService = new UserServiceImpl(connection);

                Optional<User> user = userService.authenticate(userLogin, userPassword);

                if (user.isPresent()) {
                    HttpSession session = req.getSession();
                    session.setAttribute("user", user.get());
                    getServletContext().getRequestDispatcher("/home.jsp").forward(req, resp);
                } else {
                    req.setAttribute("error", "user not found");
                    getServletContext().getRequestDispatcher("/error.jsp").forward(req, resp);
                }
            } else {
                req.setAttribute("error", "command not found");
                getServletContext().getRequestDispatcher("/error.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "command not supplied");
            getServletContext().getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
