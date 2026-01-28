package servlet;

import jakarta.servlet.http.*;
import model.Transaction;
import service.TransactionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionHistoryServlet extends HttpServlet {

	private final TransactionService service = new TransactionService();
	public static final long serialVersionUI = 6;
	private static final int Page = 1;
	private static final int Size = 20;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String accountNumber = req.getParameter("accountNumber");
		int page = Math.max(Page, Integer.parseInt(req.getParameter("page")));
		int size = Math.min(Size, Integer.parseInt(req.getParameter("size")));

		List<Transaction> transactions = null;
		try {
			transactions = service.getTransactionHistory(accountNumber, page, size);
		} catch (SQLException e) {
		    LOGGER.warn("Database error: " + e.getMessage()); 
		}

		resp.setContentType("application/json");

		for (Transaction txn : transactions) {
			resp.getWriter().println(
					txn.getTxnDate() + " | " + txn.getTxnType() + " | " + txn.getAmount() + " | " + txn.getStatus());
		}
	}
}
