package servlet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import util.QRCodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class QrCodeServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private QrCodeServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new QrCodeServlet();
    }

    @Test
    void testMissingAccountNumber() throws Exception {

        when(request.getParameter("accountNumber")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "ACCOUNT_NUMBER_REQUIRED"
        );
    }}

    