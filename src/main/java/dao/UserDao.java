package dao;

<<<<<<< HEAD
=======
import util.DBConnectionUtil;

>>>>>>> 4dd1b453739300eee62eb23ae25e880f63b07297
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

<<<<<<< HEAD
import util.DBConnectionUtil;
=======
import model.User;
>>>>>>> 4dd1b453739300eee62eb23ae25e880f63b07297

public class UserDao {

	public static String getPassword(String username) throws Exception {

<<<<<<< HEAD
		String sql = "SELECT password FROM Customer WHERE user_name = ?";
=======
		String sql = "SELECT password FROM Customer WHERE username = ?";
>>>>>>> 4dd1b453739300eee62eb23ae25e880f63b07297

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			return rs.next() ? rs.getString("password") : null;
		}
	}

}