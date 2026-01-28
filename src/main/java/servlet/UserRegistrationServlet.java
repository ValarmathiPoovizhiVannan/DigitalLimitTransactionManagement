package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.AccountDao;
import dao.CustomerDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import util.InputValidator;
import util.PasswordUtil;

public class UserRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 2;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationServlet.class);

	private final transient CustomerDao customerDao;
	private final transient AccountDao accountDao;

	public UserRegistrationServlet() {
		this.customerDao = new CustomerDao();
		this.accountDao = new AccountDao();
	}

	UserRegistrationServlet(CustomerDao customerDao, AccountDao accountDao) {
		this.customerDao = customerDao;
		this.accountDao = accountDao;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String mobile = req.getParameter("mobile");
		String email = req.getParameter("email");
		String userName = req.getParameter("username");
		String password = req.getParameter("password");

		if (!InputValidator.isValidUsername(userName) || !InputValidator.isValidPassword(password)
				|| !InputValidator.isValidEmail(email) || !InputValidator.isValidMobile(mobile)) {

			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("INVALID_INPUT");
			return;
		}
		String name = req.getParameter("name");

		try {
			String hashedPassword = PasswordUtil.hash(password);
			long customerId = customerDao.createCustomer(name, mobile, email, userName, hashedPassword);

			Account account = new Account();
			account.setCustomerId(customerId);
			account.setBalance(BigDecimal.ZERO);
			account.setDailyLimit(new BigDecimal("5000"));
			account.setMonthlyLimit(new BigDecimal("20000"));

			String accountNumber = accountDao.createAccount(account);

			resp.getWriter().write("USER_CREATED | ACCOUNT_NUMBER=" + accountNumber);

		} catch (Exception e) {
			String message = "Exception raised while authenticating user: " + e.getMessage();
			LOGGER.warn(message);
			resp.getWriter().write("USER_CREATION_FAILED");
		}
	}
}
