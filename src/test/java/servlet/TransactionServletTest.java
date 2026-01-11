package servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.TransactionService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServletTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private TransactionService service;

	private TransactionServlet servlet;
	private StringWriter stringWriter;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		servlet = new TransactionServlet();
		servlet.setTransactionService(service);

		stringWriter = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
	}

	@Test
	void testMissingParameters() throws Exception {
		when(request.getParameter("accountNumber")).thenReturn(null);

		servlet.doPost(request, response);

		assertEquals("MISSING_PARAMETERS", stringWriter.toString());
	}

	@Test
	void testInvalidAmount() throws Exception {
		when(request.getParameter("accountNumber")).thenReturn("123");
		when(request.getParameter("txnType")).thenReturn("DEBIT");
		when(request.getParameter("amount")).thenReturn("abc");

		servlet.doPost(request, response);

		assertEquals("INVALID_AMOUNT", stringWriter.toString());
	}

	@Test
	void testSuccess() throws Exception {
		when(request.getParameter("accountNumber")).thenReturn("1234");
		when(request.getParameter("txnType")).thenReturn("CREDIT");
		when(request.getParameter("amount")).thenReturn("100");

		when(service.processTransaction(eq("1234"), eq("CREDIT"), eq(new BigDecimal("100")))).thenReturn("SUCCESS");

		servlet.doPost(request, response);

		assertEquals("SUCCESS", stringWriter.toString());
	}
}
