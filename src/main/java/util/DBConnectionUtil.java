package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.AccessException;

 
public final class DBConnectionUtil {

	private static final DBUtil Dbutil = new DBUtil();

	private DBConnectionUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static Connection getConnection() throws AccessException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(Dbutil.getUrl(), Dbutil.getUser(), Dbutil.getPass());
		} catch (ClassNotFoundException | SQLException e) {
			throw new AccessException("Failed to connect a DB", e);
		}
	}
}
