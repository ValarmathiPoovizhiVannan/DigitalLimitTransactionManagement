package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.AccountDao;
import dao.TransactionDao;
import model.Account;

import java.math.BigDecimal;

public class TransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

	private final AccountDao accountDao;
	private final TransactionDao transactionDao;
	private static final String STATUS_REJECT = "REJECTED";
	private static final String STATUS_SUCCESS = "SUCCESS";


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
	                    transactionDao.insertTransaction(accId, txnType, amount, STATUS_REJECT, "DAILY_LIMIT_EXCEEDED");
	                    result = "DAILY_LIMIT_EXCEEDED";


	                } else {
	                    BigDecimal newBalance = account.getBalance().subtract(amount);
	                    accountDao.updateBalanceByAccountNumber(accountNumber, newBalance);
	                    transactionDao.insertTransaction(accId, txnType, amount, STATUS_SUCCESS, "TRANSACTION_SUCCESS");
	                    result = STATUS_SUCCESS;
	                }

	            } else if ("CREDIT".equalsIgnoreCase(txnType)) {

	                BigDecimal newBalance = account.getBalance().add(amount);
	                accountDao.updateBalanceByAccountNumber(accountNumber, newBalance);
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
}
