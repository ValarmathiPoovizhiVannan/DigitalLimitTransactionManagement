package dao;

import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserDao {
	private UserDao() {
     }

	public static String getPassword(String username) throws Exception {

		String sql = "SELECT password FROM Customer WHERE user_name = ?";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
			    return rs.getString("password");
			} else {
			    return null;
			}		}
	}

}
