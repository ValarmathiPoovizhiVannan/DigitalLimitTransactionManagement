package util;

import java.sql.Connection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class DBConnectionUtil implements ServletContextListener {

	private static HikariDataSource DATASOURCE;
	HikariConfig hconfig = new HikariConfig();

	public static final Logger LOGGER = LoggerFactory.getLogger(DBConnectionUtil.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			Properties dbprops = new Properties();
			dbprops.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

			hconfig.setDriverClassName(dbprops.getProperty("driver"));
			hconfig.setJdbcUrl(dbprops.getProperty("url"));
			hconfig.setUsername(dbprops.getProperty("username"));
			hconfig.setPassword(dbprops.getProperty("password"));
			hconfig.setMaximumPoolSize(Integer.parseInt(dbprops.getProperty("Hikari.maximumPool")));

			LOGGER.info("HikariCP initialized successfully");

		} catch (Exception e) {
			LOGGER.error("Failed to initialize datasource", e);
		}
		DATASOURCE = new HikariDataSource(hconfig);
		servletContextEvent.getServletContext().setAttribute("dataSource", DATASOURCE);
	}

	public static Connection getConnection() throws Exception {
		if (DATASOURCE == null) {
			throw new IllegalStateException("Datasource not initialized");
		}
		return DATASOURCE.getConnection();
	}

}