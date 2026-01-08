package dao;

import model.User;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

	public void register(User user) throws Exception {

		String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.executeUpdate();
		}
	}

	public String getPassword(String username) throws Exception {

		String sql = "SELECT password FROM users WHERE username = ?";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			return rs.next() ? rs.getString("password") : null;
		}
	}

}