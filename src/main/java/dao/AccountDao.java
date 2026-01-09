package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.AccessException;
import model.Account;
import util.AccountNumberGenerator;
import util.DBConnectionUtil;



public class AccountDao {
	private static final int FIRST_PARAM_INDEX = 1;
	private static final int SECOND_PARAM_INDEX = 2;
	private static final int THIRD_PARAM_INDEX = 3;
	private static final int FOURTH_PARAM_INDEX = 4;
	private static final int FIFTH_PARAM_INDEX = 5;

	public Account getAccountByAccountNumber(String accountNumber) throws SQLException {

		String sql = "SELECT account_id, account_number, balance, daily_limit, monthly_limit "
				+ "FROM account WHERE account_number = ?";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(FIRST_PARAM_INDEX, accountNumber);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Account account = new Account();
				account.setAccountId(rs.getString("account_id"));
				account.setAccountNumber(rs.getString("account_number"));
				account.setBalance(rs.getBigDecimal("balance"));
				account.setDailyLimit(rs.getBigDecimal("daily_limit"));
				account.setMonthlyLimit(rs.getBigDecimal("monthly_limit"));
				return account;
			}
		} catch (Exception e) {
			throw new AccessException("failed to get acount Number", e);
		}

		return null;
	}

	public void updateBalanceByAccountNumber(String accountNumber, BigDecimal newBalance) throws SQLException {

		String sql = "UPDATE account SET balance = ? WHERE account_number = ?";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setBigDecimal(FIRST_PARAM_INDEX, newBalance);
			ps.setString(SECOND_PARAM_INDEX, accountNumber);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new AccessException("failed to update the Balance", e);
		}

	}

	public String createAccount(Account account) throws SQLException {

		String generatedAccountNumber = AccountNumberGenerator.generateAccountNumber();

		String sql = "INSERT INTO account " + "(customer_id, account_number, balance, daily_limit, monthly_limit) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(FIRST_PARAM_INDEX, account.getCustomerId());
			ps.setString(SECOND_PARAM_INDEX, generatedAccountNumber);
			ps.setBigDecimal(THIRD_PARAM_INDEX, account.getBalance());
			ps.setBigDecimal(FOURTH_PARAM_INDEX, account.getDailyLimit());
			ps.setBigDecimal(FIFTH_PARAM_INDEX, account.getMonthlyLimit());

			ps.executeUpdate();
			return generatedAccountNumber;
		} catch (Exception e) {
			throw new AccessException("failed to create account", e);
		}
	}
}
