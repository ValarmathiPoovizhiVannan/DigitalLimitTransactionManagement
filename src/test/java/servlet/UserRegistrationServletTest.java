package servlet;

import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import dao.AccountDao;
import dao.CustomerDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;

class UserRegistrationServletTest {

     @Test
    void testUserRegistrationSuccess() throws Exception {

        CustomerDao customerDao = mock(CustomerDao.class);
        AccountDao accountDao = mock(AccountDao.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("mobile")).thenReturn("9876543210");
        when(request.getParameter("email")).thenReturn("john@test.com");
        when(request.getParameter("username")).thenReturn("john123");
        when(request.getParameter("password")).thenReturn("pass123");

        when(customerDao.createCustomer(
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(1L);

        when(accountDao.createAccount(any(Account.class)))
                .thenReturn("ACC1001");

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        UserRegistrationServlet servlet =
                new UserRegistrationServlet(customerDao, accountDao);

        servlet.doPost(request, response);

        assert sw.toString().contains("USER_CREATED");
    }

     @Test
    void testInvalidInput() throws Exception {

        CustomerDao customerDao = mock(CustomerDao.class);
        AccountDao accountDao = mock(AccountDao.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn(null);

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        UserRegistrationServlet servlet =
                new UserRegistrationServlet(customerDao, accountDao);

        servlet.doPost(request, response);

        assert sw.toString().equals("INVALID_INPUT");
        verifyNoInteractions(customerDao);
        verifyNoInteractions(accountDao);
    }

     @Test
    void testExceptionDuringUserCreation() throws Exception {

        CustomerDao customerDao = mock(CustomerDao.class);
        AccountDao accountDao = mock(AccountDao.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("mobile")).thenReturn("9876543210");
        when(request.getParameter("email")).thenReturn("john@test.com");
        when(request.getParameter("username")).thenReturn("john123");
        when(request.getParameter("password")).thenReturn("pass123");

        when(customerDao.createCustomer(
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        UserRegistrationServlet servlet =
                new UserRegistrationServlet(customerDao, accountDao);

        servlet.doPost(request, response);

        assert sw.toString().equals("USER_CREATION_FAILED");
    }
}
