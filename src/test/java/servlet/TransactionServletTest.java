package servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.TransactionService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private TransactionService service;

    private TransactionServlet servlet;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        servlet = new TransactionServlet();
        servlet.setTransactionService(service);

        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

 
    @Test
    void testInvalidParameters() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("12"); // invalid
        when(request.getParameter("amount")).thenReturn("100");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("INVALID_PARAMETERS", writer.toString());
    }

    @Test
    void testGetMissingAccountNumber() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn(null);

        servlet.doGet(request, response);

        assertEquals("MISSING_ACCOUNT_NUMBER", writer.toString());
    }

    @Test
    void testGetInvalidAccount() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1234567890");
        when(service.getAccountDetails("1234567890")).thenReturn(null);

        servlet.doGet(request, response);

        assertEquals("INVALID_ACCOUNT", writer.toString());
    }

    @Test
    void testGetAccountSuccess() throws Exception {
        Account account = new Account();
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal("5000"));
        account.setDailyLimit(new BigDecimal("10000"));

        when(request.getParameter("accountNumber")).thenReturn("1234567890");
        when(service.getAccountDetails("1234567890")).thenReturn(account);

        servlet.doGet(request, response);

        assertEquals(
                "ACCOUNT_NUMBER=1234567890 | BALANCE=5000 | DAILY_LIMIT=10000",
                writer.toString()
        );
    }
}
