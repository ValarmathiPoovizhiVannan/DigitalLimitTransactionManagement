package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import service.TransactionService;
import util.InputValidator;

import java.io.IOException;
import java.math.BigDecimal;

public final class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID=1;

	private transient TransactionService service = new TransactionService();

	void setTransactionService(TransactionService service) {
		this.service = service;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String accountNumber = req.getParameter("accountNumber");
		String amountStr = req.getParameter("amount");

		if (!InputValidator.isValidAccountNumber(accountNumber) ||
			    !InputValidator.isValidAmount(amountStr)) {
			    
			    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			    resp.getWriter().write("INVALID_PARAMETERS");
			    return;
			}


		BigDecimal amount;
		try {
			amount = new BigDecimal(amountStr);
		} catch (NumberFormatException e) {
			resp.getWriter().write("INVALID_AMOUNT");
			return;
		}
		String txnType = req.getParameter("txnType");

		String result = service.processTransaction(accountNumber, txnType, amount);

		resp.getWriter().write(result);
	}
	@Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {

        String accountNumber = req.getParameter("accountNumber");

        if (accountNumber == null) {
            resp.getWriter().write("MISSING_ACCOUNT_NUMBER");
            return;
        }

        try {
            Account account = service.getAccountDetails(accountNumber);

            if (account == null) {
                resp.getWriter().write("INVALID_ACCOUNT");
                return;
            }

            resp.getWriter().write(
                "ACCOUNT_NUMBER=" + account.getAccountNumber() +
                " | BALANCE=" + account.getBalance() +
                " | DAILY_LIMIT=" + account.getDailyLimit()             );

        } catch (Exception e) {
            resp.getWriter().write("FAILED");
        }
    }

}
