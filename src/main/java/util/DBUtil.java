package util;

public class DBUtil {

	private static final String URL = "jdbc:mysql://localhost:3306/fintech";
	private static final String USER = "root";
	private static final String PASS = "Valar@123";

	public String getUrl() {
		return URL;
	}

	public String getUser() {
		return USER;
	}

	public String getPass() {
		return PASS;
	}

}
