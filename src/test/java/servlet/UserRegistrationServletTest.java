package servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.AccountDao;
import dao.CustomerDao;

import java.io.PrintWriter;
import java.io.StringWriter;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRegistrationServletTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private CustomerDao customerDao;

	@Mock
	private AccountDao accountDao;

	private UserRegistrationServlet servlet;
	private StringWriter writer;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		servlet = new UserRegistrationServlet(customerDao, accountDao);

		writer = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(writer));
	}

	@Test
	void testInvalidInput() throws Exception {
		when(request.getParameter("name")).thenReturn(null);
		when(request.getParameter("mobile")).thenReturn("9999999999");
		when(request.getParameter("email")).thenReturn("test@mail.com");

		servlet.doPost(request, response);

		assertEquals("INVALID_INPUT", writer.toString(),"tes the Invalid input of Customer creation");

		verifyNoInteractions(customerDao, accountDao);
	}

	@Test
	void testSuccessfulUserRegistration() throws Exception {
		when(request.getParameter("name")).thenReturn("John");
		when(request.getParameter("mobile")).thenReturn("9999999999");
		when(request.getParameter("email")).thenReturn("john@test.com");

		when(customerDao.createCustomer("John", "9999999999", "john@test.com")).thenReturn(101L);

		when(accountDao.createAccount(any(Account.class))).thenReturn("ACC123");

		servlet.doPost(request, response);

		assertEquals("USER_CREATED | ACCOUNT_NUMBER=ACC123", writer.toString(),"Test the success message of customer creation");

		verify(customerDao, times(1)).createCustomer("John", "9999999999", "john@test.com");

		verify(accountDao, times(1)).createAccount(any(Account.class));
	}

	@Test
	void testUserCreationFailed() throws Exception {
		when(request.getParameter("name")).thenReturn("John");
		when(request.getParameter("mobile")).thenReturn("9999999999");
		when(request.getParameter("email")).thenReturn("john@test.com");

		when(customerDao.createCustomer(any(), any(), any())).thenThrow(new RuntimeException("DB error"));

		servlet.doPost(request, response);

		assertEquals("USER_CREATION_FAILED", writer.toString(),"Test the user creation failed");

		verify(customerDao, times(1)).createCustomer(any(), any(), any());

		verifyNoInteractions(accountDao);
	}
}
