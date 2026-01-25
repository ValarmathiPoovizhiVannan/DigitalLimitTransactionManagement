package servlet;

import jakarta.servlet.http.*;
import model.Transaction;
import service.TransactionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
 
public class TransactionHistoryServlet extends HttpServlet {

    private final TransactionService service = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String accountNumber = req.getParameter("accountNumber");
        int page = Math.max(1, Integer.parseInt(req.getParameter("page")));
        int size = Math.min(20, Integer.parseInt(req.getParameter("size")));

        List<Transaction> transactions = null;
		try {
			transactions = service.getTransactionHistory(accountNumber, page, size);
		} catch (SQLException e) {
 			e.printStackTrace();
		}

		resp.setContentType("application/json");

        for (Transaction txn : transactions) {
            resp.getWriter().println(
                txn.getTxnDate() + " | " +
                txn.getTxnType() + " | " +
                txn.getAmount() + " | " +
                txn.getStatus()
            );
        }
    }
}
