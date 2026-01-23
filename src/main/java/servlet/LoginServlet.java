package servlet;

import java.io.IOException;

import dao.CustomerDao;
import dao.UserDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 import util.PasswordUtil;

public class LoginServlet extends HttpServlet {

 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
			String hashedPassword= UserDao.getPassword(username);
			if(hashedPassword!=null && PasswordUtil.match(password, hashedPassword)) {
			    resp.getWriter().write("LOGIN_SUCCESS");				
			}else {
				resp.getWriter().write("Invalid UserName and Password");
			}
		} catch (Exception e) {
 			e.printStackTrace();
		}

       
    }
}
