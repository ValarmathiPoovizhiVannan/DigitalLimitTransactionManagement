package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import util.DBConnectionUtil;

 
class CustomerDaoTest {

	@Test
	void testCreateCustomerSuccess() throws Exception {
		Connection con = mock(Connection.class);
		PreparedStatement ps = mock(PreparedStatement.class);
		ResultSet rs = mock(ResultSet.class);

		try (MockedStatic<DBConnectionUtil> mocked = mockStatic(DBConnectionUtil.class)) {
			mocked.when(DBConnectionUtil::getConnection).thenReturn(con);
			when(con.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(ps);
			when(ps.executeUpdate()).thenReturn(1);
			when(ps.getGeneratedKeys()).thenReturn(rs);
			when(rs.next()).thenReturn(true);
			when(rs.getLong(1)).thenReturn(101L);

			CustomerDao dao = new CustomerDao();
			long customerId = dao.createCustomer("John Doe", "1234567890", "john@example.com");

			assertEquals(101L, customerId, "Customer creation success");

			verify(ps).setString(1, "John Doe");
			verify(ps).setString(2, "1234567890");
			verify(ps).setString(3, "john@example.com");
			verify(ps).executeUpdate();
			verify(ps).getGeneratedKeys();
			verify(rs).next();
			verify(rs).getLong(1);
		}
	}

	@Test
	void testCreateCustomerFailure() throws Exception {
		Connection con = mock(Connection.class);
		PreparedStatement ps = mock(PreparedStatement.class);
		ResultSet rs = mock(ResultSet.class);
		try (MockedStatic<DBConnectionUtil> mocked = mockStatic(DBConnectionUtil.class)) {
			mocked.when(DBConnectionUtil::getConnection).thenReturn(con);
			when(con.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(ps);
			when(ps.executeUpdate()).thenReturn(1);
			when(ps.getGeneratedKeys()).thenReturn(rs);
			when(rs.next()).thenReturn(false);

			CustomerDao dao = new CustomerDao();
			Exception exception = assertThrows(Exception.class,
					() -> dao.createCustomer("John Doe", "1234567890", "john@example.com"),
					"Throw exception if any of the feild is missing");

			assertEquals("Customer creation failed", exception.getMessage(), "customer creation failed");
		}
	}
}
