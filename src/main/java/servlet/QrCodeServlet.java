package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.QRCodeUtil;

import java.io.IOException;

 public class QrCodeServlet extends HttpServlet {
	 public static final long serialVersionUI= 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String accountNumber = req.getParameter("accountNumber");

        if (accountNumber == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ACCOUNT_NUMBER_REQUIRED");
            return;
        }

         String qrData = "upi://pay?acc=" + accountNumber;
         String amount = req.getParameter("amount");

        if (amount != null) {
            qrData += "&amt=" + amount;
        }

        resp.setContentType("image/png");

        try {
            QRCodeUtil.generateQRCode(qrData, resp.getOutputStream());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "QR_GENERATION_FAILED");
        }
    }
}
