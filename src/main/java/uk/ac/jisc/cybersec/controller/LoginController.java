
package uk.ac.jisc.cybersec.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Demonstrate session fixation.*/
@RestController
@RequestMapping(value = "/")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/sf-login", method = RequestMethod.GET)
    public String pageOrLogin(final HttpServletRequest req) {

        final HttpSession session = req.getSession();

        log.info("Login controller activated with id " + session.getId());

        final Object sessionObj = session.getAttribute("authenticated");
        if (sessionObj != null && sessionObj instanceof Boolean && sessionObj == Boolean.TRUE) {
            return "<h1>You have authenticated with session " + session.getId() + "</h1>";

        } else {

            return "<h1>Login</h1>" + "<form action=\"/sf-login;jsessionid=" + session.getId() + "\" method=\"post\">"
                    + "<label for=\"uname\">Username:</label><input id=\"uname\" name=\"uname\"/><br/>"
                    + "<label for=\"pword\">Password:</label><input type=\"password\" id=\"pword\" name=\"pword\"/><br/>"
                    + "<input type=\"submit\" value=\"Submit\">" + "</form> ";
        }
    }

    @RequestMapping(value = "/sf-login", method = RequestMethod.POST)
    public String login(final HttpServletRequest req, final HttpServletResponse resp,
            @RequestParam("uname") String username, @RequestParam("pword") String password) {

        final HttpSession session = req.getSession(false);
        if (session == null) {
            try {
                resp.sendRedirect("/sf-login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("Login with user {} from id {}", username, session);

        final Object sessionObj = session.getAttribute("authenticated");

        if (sessionObj != null && sessionObj instanceof Boolean && sessionObj == Boolean.TRUE) {
            try {
                resp.sendRedirect("/sf-login;jsessionid" + session.getId());
            } catch (IOException e) {

            }
        }

        if (username.equals("bob") && password.equals("bob")) {
            session.setAttribute("authenticated", Boolean.TRUE);

        }
        try {
            resp.sendRedirect("/sf-login;jsessionid=" + session.getId());
        } catch (IOException e) {

        }
        return "nothing";
    }

}
