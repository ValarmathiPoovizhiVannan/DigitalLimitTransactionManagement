package dao;

import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;

public class UserDao {

	public static String getPassword(String username) throws Exception {

		String sql = "SELECT password FROM Customer WHERE username = ?";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			return rs.next() ? rs.getString("password") : null;
		}
	}

}