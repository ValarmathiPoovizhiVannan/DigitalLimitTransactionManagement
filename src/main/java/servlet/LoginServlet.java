package servlet;

import java.io.IOException;
<<<<<<< HEAD
import dao.UserDao;import jakarta.servlet.http.HttpServlet;
import jakarta
.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.PasswordUtil;
  
public class LoginServlet extends HttpServlet {

     @Override
     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
             throws IOException {

         resp.setContentType("text/plain");

         String username = req.getParameter("username");
         String password = req.getParameter("password");

         try {
             String hashedPassword = UserDao.getPassword(username);

             if (hashedPassword != null && PasswordUtil.match(password, hashedPassword)) {

                  HttpSession session = req.getSession(true);
                 session.setAttribute("username", username);
                 session.setMaxInactiveInterval(30 * 60); 

                 resp.getWriter().write("LOGIN_SUCCESS");
             } else {
                 resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                 resp.getWriter().write("Invalid UserName and Password");
             }

         } catch (Exception e) {
             resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             resp.getWriter().write("Internal Server Error");
         }
     }
 
=======

import dao.CustomerDao;
import dao.UserDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 import util.PasswordUtil;

@WebServlet("/login")
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
>>>>>>> 4dd1b453739300eee62eb23ae25e880f63b07297
}