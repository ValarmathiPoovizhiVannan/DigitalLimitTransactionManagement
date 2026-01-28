package servlet;

import dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JwtUtil;
import util.PasswordUtil;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
	 public static final long serialVersionUI= 4;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            String hashedPassword = UserDao.getPassword(username);

            if (hashedPassword != null && PasswordUtil.match(password, hashedPassword)) {

                 String token = JwtUtil.generateToken(username);

                resp.setContentType("application/json");
                resp.getWriter().write("{\"token\":\"" + token + "\"}");

            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("INVALID_CREDENTIALS");
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("LOGIN_FAILED");
        }
    }
}
