package util;

import java.sql.Connection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import exceptions.AccessException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.io.InputStream;

public class DBConnectionUtil implements ServletContextListener {

    private static HikariDataSource DATASOURCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnectionUtil.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	try (InputStream is = Thread.currentThread()
    	        .getContextClassLoader()
    	        .getResourceAsStream("application.properties")) {

            if (is == null) {
                throw new AccessException("application.properties not found");
            }

            Properties props = new Properties();
            props.load(is);

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("app.datasource.driver-class-name"));
            config.setJdbcUrl(props.getProperty("app.datasource.url"));
            config.setUsername(props.getProperty("app.datasource.username"));
            config.setPassword(props.getProperty("app.datasource.password"));

            config.setMaximumPoolSize(
                    Integer.parseInt(props.getProperty(
                            "app.datasource.hikari.maximum-pool-size", "10"))
            );

            DATASOURCE = new HikariDataSource(config);
            event.getServletContext().setAttribute("dataSource", DATASOURCE);

            LOGGER.info("Application HikariCP datasource initialized (fintech DB)");

        } catch (Exception e) {
            LOGGER.error("Failed to initialize Application datasource", e);
            throw new AccessException(e); 
        }
    }

    public static Connection getConnection() throws Exception {
        if (DATASOURCE == null) {
            throw new IllegalStateException("Datasource not initialized");
        }
        return DATASOURCE.getConnection();
    }
}
