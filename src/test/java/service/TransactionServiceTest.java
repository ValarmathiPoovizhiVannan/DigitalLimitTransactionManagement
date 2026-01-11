package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.AccountDao;
import dao.TransactionDao;
import model.Account;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

	@Mock
	private AccountDao accountDao;

	@Mock
	private TransactionDao transactionDao;

	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		transactionService = new TransactionService(accountDao, transactionDao);
	}

	@Test
	void testInvalidAccount() throws Exception {
		when(accountDao.getAccountByAccountNumber("ACC1")).thenReturn(null);

		String result = transactionService.processTransaction("ACC1", "DEBIT", new BigDecimal("100"));

		assertEquals("INVALID_ACCOUNT", result,"Test the Invalid Account");
		verifyNoInteractions(transactionDao);
	}

	@Test
	void testInsufficientBalance() throws Exception {
		Account account = mock(Account.class);

		when(account.getAccountId()).thenReturn("1");
		when(account.getBalance()).thenReturn(new BigDecimal("100"));
		when(account.getDailyLimit()).thenReturn(new BigDecimal("5000"));
		when(account.getMonthlyLimit()).thenReturn(new BigDecimal("20000"));

		when(accountDao.getAccountByAccountNumber("ACC1")).thenReturn(account);

		String result = transactionService.processTransaction("ACC1", "DEBIT", new BigDecimal("200"));

		assertEquals("INSUFFICIENT_BALANCE", result,"Test the balance");

		verify(transactionDao).insertTransaction(1L, "DEBIT", new BigDecimal("200"), "REJECTED",
				"INSUFFICIENT_BALANCE");
	}

	@Test
	void testDailyLimitExceeded() throws Exception {
		Account account = mock(Account.class);

		when(account.getAccountId()).thenReturn("1");
		when(account.getBalance()).thenReturn(new BigDecimal("10000"));
		when(account.getDailyLimit()).thenReturn(new BigDecimal("100"));
		when(account.getMonthlyLimit()).thenReturn(new BigDecimal("20000"));

		when(accountDao.getAccountByAccountNumber("ACC1")).thenReturn(account);

		String result = transactionService.processTransaction("ACC1", "DEBIT", new BigDecimal("500"));

		assertEquals("DAILY_LIMIT_EXCEEDED", result,"test the Daily Limit of transaction");

		verify(transactionDao).insertTransaction(1L, "DEBIT", new BigDecimal("500"), "REJECTED",
				"DAILY_LIMIT_EXCEEDED");
	}

	@Test
	void testDebitSuccess() throws Exception {
		Account account = mock(Account.class);

		when(account.getAccountId()).thenReturn("1");
		when(account.getBalance()).thenReturn(new BigDecimal("1000"));
		when(account.getDailyLimit()).thenReturn(new BigDecimal("5000"));
		when(account.getMonthlyLimit()).thenReturn(new BigDecimal("20000"));

		when(accountDao.getAccountByAccountNumber("ACC1")).thenReturn(account);

		String result = transactionService.processTransaction("ACC1", "DEBIT", new BigDecimal("200"));

		assertEquals("SUCCESS", result, "Test the debit success");

		verify(accountDao).updateBalanceByAccountNumber("ACC1", new BigDecimal("800"));

		verify(transactionDao).insertTransaction(1L, "DEBIT", new BigDecimal("200"), "SUCCESS", "TRANSACTION_SUCCESS");
	}

	@Test
	void testCreditSuccess() throws Exception {
		Account account = mock(Account.class);

		when(account.getAccountId()).thenReturn("1");
		when(account.getBalance()).thenReturn(new BigDecimal("500"));

		when(accountDao.getAccountByAccountNumber("ACC1")).thenReturn(account);

		String result = transactionService.processTransaction("ACC1", "CREDIT", new BigDecimal("200"));

		assertEquals("SUCCESS", result,"Test the credit success");

		verify(accountDao).updateBalanceByAccountNumber("ACC1", new BigDecimal("700"));

		verify(transactionDao).insertTransaction(1L, "CREDIT", new BigDecimal("200"), "SUCCESS", "TRANSACTION_SUCCESS");
	}

	@Test
	void testInvalidTransactionType() throws Exception {
		Account account = mock(Account.class);

		when(account.getAccountId()).thenReturn("1");
		when(account.getBalance()).thenReturn(new BigDecimal("500"));

		when(accountDao.getAccountByAccountNumber("ACC1")).thenReturn(account);

		String result = transactionService.processTransaction("ACC1", "TRANSFER", new BigDecimal("100"));

		assertEquals("INVALID_TXN_TYPE", result,"Test the valid Transaction Type");

		verifyNoInteractions(transactionDao);
	}

	@Test
	void testExceptionHandling() throws Exception {
		when(accountDao.getAccountByAccountNumber(any())).thenThrow(new RuntimeException("DB error"));

		String result = transactionService.processTransaction("ACC1", "DEBIT", new BigDecimal("100"));

		assertEquals("FAILED", result, "test the Exception Handling");
	}
}
