package dao;

import util.DBConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import exceptions.AccessException;
import model.Transaction;

public class TransactionDao {
	private static final int FIRST_PARAM_INDEX = 1;
	private static final int SECOND_PARAM_INDEX = 2;
	private static final int THIRD_PARAM_INDEX = 3;
	private static final int FOURTH_PARAM_INDEX = 4;
	private static final int FIFTH_PARAM_INDEX = 5;

	public void insertTransaction(long accountId, String txnType, BigDecimal amount, String status, String reason)
			throws AccessException {

		String sql = "INSERT INTO transaction_history " + "(account_id, txn_type, amount, txn_date, status, reason) "
				+ "VALUES (?, ?, ?, CURRENT_DATE, ?, ?)";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(FIRST_PARAM_INDEX, accountId);
			ps.setString(SECOND_PARAM_INDEX, txnType);
			ps.setBigDecimal(THIRD_PARAM_INDEX, amount);
			ps.setString(FOURTH_PARAM_INDEX, status);
			ps.setString(FIFTH_PARAM_INDEX, reason);

			ps.executeUpdate();
		} catch (Exception e) {
			throw new AccessException("Customer creation failed", e);
		}
	}
	
	public List<Transaction> getTransactionsByAccount(
	        long accountId, int page, int size) {

	    int offset = (page - 1) * size;

	    String sql = """
	        SELECT txn_id, account_id, txn_type, amount, txn_date, status, reason
	        FROM transaction_history
	        WHERE account_id = ?
	        ORDER BY txn_date DESC
	        LIMIT ? OFFSET ?
	    """;

	    List<Transaction> transactions = new ArrayList<>();

	    try (Connection con = DBConnectionUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setLong(FIRST_PARAM_INDEX, accountId);
	        ps.setInt(SECOND_PARAM_INDEX, size);
	        ps.setInt(THIRD_PARAM_INDEX, offset);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            transactions.add(new Transaction(
	                rs.getLong("txn_id"),
	                rs.getLong("account_id"),
	                rs.getString("txn_type"),
	                rs.getBigDecimal("amount"),
	                rs.getDate("txn_date").toLocalDate(),
	                rs.getString("status"),
	                rs.getString("reason")
	            ));
	        }

	    } catch (Exception e) {
	        throw new AccessException("Failed to fetch transactions", e);
	    }

	    return transactions;
	}

}
