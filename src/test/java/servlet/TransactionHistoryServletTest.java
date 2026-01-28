package servlet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.*;
import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.TransactionService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class TransactionHistoryServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private TransactionService service;

    private TransactionHistoryServlet servlet;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        servlet = new TransactionHistoryServlet();

         Field field = TransactionHistoryServlet.class.getDeclaredField("service");
        field.setAccessible(true);
        field.set(servlet, service);

        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Test
    void testTransactionHistorySuccess() throws Exception {
         when(request.getParameter("accountNumber")).thenReturn("10001");
        when(request.getParameter("page")).thenReturn("0"); 
        when(request.getParameter("size")).thenReturn("2");

         Transaction t1 = new Transaction(1L, 10001L, "CREDIT", new BigDecimal("1000"), LocalDate.now(), "SUCCESS", "Salary");
        Transaction t2 = new Transaction(2L, 10001L, "DEBIT", new BigDecimal("500"), LocalDate.now(), "SUCCESS", "ATM Withdrawal");

        List<Transaction> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);

         when(service.getTransactionHistory("10001", 1, 2)).thenReturn(list);

        servlet.doGet(request, response);

         verify(response).setContentType("application/json");

        String output = writer.toString();
        assertTrue(output.contains("CREDIT"));
        assertTrue(output.contains("DEBIT"));
        assertTrue(output.contains("1000"));
        assertTrue(output.contains("500"));
        assertTrue(output.contains("SUCCESS"));
    }
}
