package servlet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import util.JwtUtil;
import util.PasswordUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private LoginServlet servlet;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new LoginServlet();
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        when(request.getParameter("username")).thenReturn("john");
        when(request.getParameter("password")).thenReturn("password123");

        // Mock static methods
        try (MockedStatic<UserDao> mockedUserDao = mockStatic(UserDao.class);
             MockedStatic<PasswordUtil> mockedPasswordUtil = mockStatic(PasswordUtil.class);
             MockedStatic<JwtUtil> mockedJwt = mockStatic(JwtUtil.class)) {

            mockedUserDao.when(() -> UserDao.getPassword("john")).thenReturn("hashedPassword");
            mockedPasswordUtil.when(() -> PasswordUtil.match("password123", "hashedPassword")).thenReturn(true);
            mockedJwt.when(() -> JwtUtil.generateToken("john")).thenReturn("mockedToken");

            servlet.doPost(request, response);

            verify(response).setContentType("application/json");
            assertTrue(responseWriter.toString().contains("\"token\":\"mockedToken\""));
        }
    }

    @Test
    void testInvalidCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("john");
        when(request.getParameter("password")).thenReturn("wrongpass");

        try (MockedStatic<UserDao> mockedUserDao = mockStatic(UserDao.class);
             MockedStatic<PasswordUtil> mockedPasswordUtil = mockStatic(PasswordUtil.class)) {

            mockedUserDao.when(() -> UserDao.getPassword("john")).thenReturn("hashedPassword");
            mockedPasswordUtil.when(() -> PasswordUtil.match("wrongpass", "hashedPassword")).thenReturn(false);

            servlet.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            assertEquals("INVALID_CREDENTIALS", responseWriter.toString());
        }
    }

    @Test
    void testLoginException() throws Exception {
        when(request.getParameter("username")).thenReturn("john");
        when(request.getParameter("password")).thenReturn("password123");

        try (MockedStatic<UserDao> mockedUserDao = mockStatic(UserDao.class)) {
            mockedUserDao.when(() -> UserDao.getPassword("john")).thenThrow(new RuntimeException("DB error"));

            servlet.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            assertEquals("LOGIN_FAILED", responseWriter.toString());
        }
    }
}
