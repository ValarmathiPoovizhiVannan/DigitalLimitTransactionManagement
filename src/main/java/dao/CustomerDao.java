package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.AccessException;
import util.DBConnectionUtil;


public class CustomerDao {
	private static final int FIRST_PARAM_INDEX = 1;
	private static final int SECOND_PARAM_INDEX = 2;
	private static final int THIRD_PARAM_INDEX = 3;
	private static final int FOURTH_PARAM_INDEX = 4;
	private static final int FIFTH_PARAM_INDEX = 5;

	public long createCustomer(String name, String mobile, String email, String user_name, String password)
			throws SQLException {

		String sql = "INSERT INTO customer (name, mobile, email,user_name,password) VALUES (?, ?, ?, ? , ? )";

		try (Connection con = DBConnectionUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(FIRST_PARAM_INDEX, name);
			ps.setString(SECOND_PARAM_INDEX, mobile);
			ps.setString(THIRD_PARAM_INDEX, email);
			ps.setString(FOURTH_PARAM_INDEX, user_name);
			ps.setString(FIFTH_PARAM_INDEX, password);

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			}

		} catch (Exception e) {
			throw new AccessException("Customer creation failed");
		}

		throw new AccessException("Customer creation failed");

	}

}
