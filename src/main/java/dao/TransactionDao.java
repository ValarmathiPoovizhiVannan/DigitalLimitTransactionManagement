package dao;

import util.DBConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import exceptions.AccessException;

 
public class TransactionDao {
	private static  final int FIRST_PARAM_INDEX = 1;
	private static final int SECOND_PARAM_INDEX = 2;
	private static final int THIRD_PARAM_INDEX = 3;
	private static final int FOURTH_PARAM_INDEX = 4;
	private static final int FIFTH_PARAM_INDEX = 5;
	public void insertTransaction(long accountId, String txnType, BigDecimal amount, String status, String reason) throws AccessException {

	String sql = "INSERT INTO transaction_history " + "(account_id, txn_type, amount, txn_date, status, reason) "
				+ "VALUES (?, ?, ?, CURRENT_DATE, ?, ?)";

		try (Connection con = DBConnectionUtil.getConnection(); 
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(FIRST_PARAM_INDEX, accountId);
			ps.setString(SECOND_PARAM_INDEX, txnType);
			ps.setBigDecimal(THIRD_PARAM_INDEX, amount);
			ps.setString(FOURTH_PARAM_INDEX, status);
			ps.setString(FIFTH_PARAM_INDEX, reason);

			ps.executeUpdate();
		} catch (Exception  e) {
			throw new AccessException("Customer creation failed",e);
		}
	}
}
