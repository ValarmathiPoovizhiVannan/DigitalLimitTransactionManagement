package dao;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import util.DBConnectionUtil;

class TransactionDaoTest {

	@Test
	void testInsertTransaction() throws Exception {
		Connection con = mock(Connection.class);
		PreparedStatement ps = mock(PreparedStatement.class);

		try (MockedStatic<DBConnectionUtil> mocked = mockStatic(DBConnectionUtil.class)) {
			mocked.when(DBConnectionUtil::getConnection).thenReturn(con);
			when(con.prepareStatement(anyString())).thenReturn(ps);

			when(ps.executeUpdate()).thenReturn(1);

			TransactionDao dao = new TransactionDao();
			dao.insertTransaction(1L, "DEBIT", new BigDecimal("500.00"), "SUCCESS", "Test transaction");

			verify(ps).setLong(1, 1L);
			verify(ps).setString(2, "DEBIT");
			verify(ps).setBigDecimal(3, new BigDecimal("500.00"));
			verify(ps).setString(4, "SUCCESS");
			verify(ps).setString(5, "Test transaction");
			verify(ps).executeUpdate();
		}
	}

}
