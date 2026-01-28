package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.AccountDao;
import dao.TransactionDao;
import exceptions.AccessException;
import model.Account;
import model.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

	private final AccountDao accountDao;
	private final TransactionDao transactionDao;
	private static final String STATUS_REJECT = "REJECTED";
	private static final String STATUS_SUCCESS = "SUCCESS";
	 private static final String Daily_Limit= "DAILY_LIMIT_EXCEEDED";

	public TransactionService() {
		this.accountDao = new AccountDao();
		this.transactionDao = new TransactionDao();
	}

	public TransactionService(AccountDao accountDao, TransactionDao transactionDao) {
		this.accountDao = accountDao;
		this.transactionDao = transactionDao;
	}

	public String processTransaction(String accountNumber, String txnType, BigDecimal amount) {

		String result = null;

		try {
			Account account = accountDao.getAccountByAccountNumber(accountNumber);

			if (account == null) {
				LOGGER.warn("Invalid account {}", accountNumber);
				result = "INVALID_ACCOUNT";
				
			} else {

				long accId = Long.parseLong(account.getAccountId());

				if ("DEBIT".equalsIgnoreCase(txnType)) {

					if (amount.compareTo(account.getBalance()) > 0) {
						transactionDao.insertTransaction(accId, txnType, amount, STATUS_REJECT, "INSUFFICIENT_BALANCE");
						result = "INSUFFICIENT_BALANCE";

					} else if (amount.compareTo(account.getDailyLimit()) > 0) {
						transactionDao.insertTransaction(accId, txnType, amount, STATUS_REJECT, Daily_Limit);
						result = Daily_Limit;

					} else {
						BigDecimal newBalance = account.getBalance().subtract(amount);
						updateDailyLimit(account, amount);
						accountDao.updateBalanceByAccountNumber(accountNumber, newBalance,account.getDailyLimit());
						transactionDao.insertTransaction(accId, txnType, amount, STATUS_SUCCESS, "TRANSACTION_SUCCESS");
						result = STATUS_SUCCESS;
					}

				} else if ("CREDIT".equalsIgnoreCase(txnType)) {

					BigDecimal newBalance = account.getBalance().add(amount);
					accountDao.updateBalanceByAccountNumber(accountNumber, newBalance,account.getDailyLimit());
					transactionDao.insertTransaction(accId, txnType, amount, STATUS_SUCCESS, "TRANSACTION_SUCCESS");
					result = STATUS_SUCCESS;

				} else {
					result = "INVALID_TXN_TYPE";
				}
			}

		} catch (Exception e) {
			LOGGER.error("Transaction failed", e);
			result = "FAILED";
		} finally {
			LOGGER.info("Transaction successful for account");
		}

		return result;
	}

	public Account getAccountDetails(String accountNumber) throws Exception {
		return accountDao.getAccountByAccountNumber(accountNumber);
	}

	public void updateDailyLimit(Account account, BigDecimal amount) {

		if (account == null) {
			throw new IllegalArgumentException("Account cannot be null");
		}

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Invalid transaction amount");
		}

		BigDecimal currentDailyLimit = account.getDailyLimit();

		if (currentDailyLimit.compareTo(amount) < 0) {
			throw new AccessException(Daily_Limit);
		}

		BigDecimal updatedDailyLimit = currentDailyLimit.subtract(amount);

		account.setDailyLimit(updatedDailyLimit);
	}
	public List<Transaction> getTransactionHistory(
	        String accountNumber, int page, int size) throws SQLException {

	    Account account = accountDao.getAccountByAccountNumber(accountNumber);

	    if (account == null) {
	        throw new AccessException("INVALID_ACCOUNT");
	    }

	    long accountId = Long.parseLong(account.getAccountId());

	    return transactionDao.getTransactionsByAccount(accountId, page, size);
	}

}
