package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import model.Account;
import util.DBConnectionUtil;



class AccountDaoTest {

	@Test
	void testGetAccountByAccountNumber() throws Exception {

		Connection con = mock(Connection.class);
		PreparedStatement ps = mock(PreparedStatement.class);
		ResultSet rs = mock(ResultSet.class);

		try (MockedStatic<DBConnectionUtil> mocked = mockStatic(DBConnectionUtil.class)) {

			mocked.when(DBConnectionUtil::getConnection).thenReturn(con);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(ps.executeQuery()).thenReturn(rs);

			when(rs.next()).thenReturn(true);
			when(rs.getString("account_id")).thenReturn("1");
			when(rs.getString("account_number")).thenReturn("ACC123");
			when(rs.getBigDecimal("balance")).thenReturn(new BigDecimal("1000"));
			when(rs.getBigDecimal("daily_limit")).thenReturn(new BigDecimal("500"));
			when(rs.getBigDecimal("monthly_limit")).thenReturn(new BigDecimal("10000"));

			AccountDao dao = new AccountDao();
			Account account = dao.getAccountByAccountNumber("ACC123");

			assertNotNull(account,"Account should not be null");
			assertEquals("ACC123", account.getAccountNumber(),"Get account by account Number");
			assertEquals(new BigDecimal("1000"), account.getBalance(),"Get balance from the account");
		}
	}

	@Test
	void testGetAccountByAccountNumberNotFound() throws Exception {

		Connection con = mock(Connection.class);
		PreparedStatement ps = mock(PreparedStatement.class);
		ResultSet rs = mock(ResultSet.class);

		try (MockedStatic<DBConnectionUtil> mocked = mockStatic(DBConnectionUtil.class)) {

			mocked.when(DBConnectionUtil::getConnection).thenReturn(con);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(ps.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(false);

			AccountDao dao = new AccountDao();
			Account account = dao.getAccountByAccountNumber("INVALID");

			assertNull(account,"Account number is not found");
		}
	}

//	@Test
//	void testUpdateBalanceByAccountNumber() throws Exception {
//
//		Connection con = mock(Connection.class);
//		PreparedStatement ps = mock(PreparedStatement.class);
//
//		try (MockedStatic<DBConnectionUtil> mocked = mockStatic(DBConnectionUtil.class)) {
//
//			mocked.when(DBConnectionUtil::getConnection).thenReturn(con);
//			when(con.prepareStatement(anyString())).thenReturn(ps);
//			when(ps.executeUpdate()).thenReturn(1);
//
//			AccountDao dao = new AccountDao();
//			dao.updateBalanceByAccountNumber("ACC123", new BigDecimal("2000"),new BigDecimal("1000"));
//
//			verify(ps).setBigDecimal(1, new BigDecimal("2000"));
//			verify(ps).setBigDecimal(2, new BigDecimal("1000"));
//
//			verify(ps).setString(3, "ACC123");
//			verify(ps).executeUpdate();
//		}
//	}

	@Test
	void testCreateAccount() throws Exception {

		Connection con = mock(Connection.class);
		PreparedStatement ps = mock(PreparedStatement.class);

		try (MockedStatic<DBConnectionUtil> mocked = mockStatic(DBConnectionUtil.class)) {

			mocked.when(DBConnectionUtil::getConnection).thenReturn(con);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(ps.executeUpdate()).thenReturn(1);

			Account account = new Account();
			account.setCustomerId(1L);
			account.setBalance(new BigDecimal("3000"));
			account.setDailyLimit(new BigDecimal("1000"));
			account.setMonthlyLimit(new BigDecimal("20000"));

			AccountDao dao = new AccountDao();
			String accNo = dao.createAccount(account);

			assertNotNull(accNo,"If account number is null ,account creation is failed");
			verify(ps).executeUpdate();
		}
	}
}
