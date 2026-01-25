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
    }

    @Test
    void testQrCodeGeneratedSuccessfully() throws Exception {

        when(request.getParameter("accountNumber")).thenReturn("1234567890");
        when(request.getParameter("amount")).thenReturn("500");

        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        try (MockedStatic<QRCodeUtil> mockedQR = mockStatic(QRCodeUtil.class)) {

            servlet.doGet(request, response);

            verify(response).setContentType("image/png");

            mockedQR.verify(() ->
                    QRCodeUtil.generateQRCode(
                            "upi://pay?acc=1234567890&amt=500",
                            outputStream
                    )
            );
        }
    }

    @Test
    void testQrGenerationFailure() throws Exception {

        when(request.getParameter("accountNumber")).thenReturn("1234567890");

        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        try (MockedStatic<QRCodeUtil> mockedQR = mockStatic(QRCodeUtil.class)) {

            mockedQR.when(() ->
                    QRCodeUtil.generateQRCode(anyString(), any())
            ).thenThrow(new RuntimeException("QR error"));

            servlet.doGet(request, response);

            verify(response).sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "QR_GENERATION_FAILED"
            );
        }
    }
}
