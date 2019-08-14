package dev.foolen.playtime.databases.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.bukkit.configuration.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import dev.foolen.playtime.PlayTimePlugin;
import dev.foolen.playtime.databases.PlayerDB;
import dev.foolen.playtime.utils.Logger;

public class MySQL {
	private final String HOSTNAME;
	private final String PORT;
	private final String DATABASE;
	private final String USERNAME;
	private final String PASSWORD;

	private static HikariDataSource datasource;

	public MySQL() {
		Configuration config = PlayTimePlugin.getInstance().getConfig();

		HOSTNAME = config.getString("database.hostname");
		PORT = config.getString("database.port");
		DATABASE = config.getString("database.database");
		USERNAME = config.getString("database.username");
		PASSWORD = config.getString("database.password");

		connect();

		loadDatabases();
	}

	private void connect() {
		Properties props = new Properties();
		props.setProperty("jdbcUrl", "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE);
		props.setProperty("username", USERNAME);
		props.setProperty("password", PASSWORD);
		
		props.setProperty("dataSource.dataSourceClassName", "com.mysql.cj.jdbc.MysqlDataSource");
		props.setProperty("dataSource.maximumPoolSize", "10");
		props.setProperty("dataSource.cachePrepStmts", "true");
		props.setProperty("dataSource.prepStmtCacheSize", "250");
		props.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		props.setProperty("dataSource.useServerPrepStmts", "true");
		props.setProperty("dataSource.useLocalSessionState", "true");
		props.setProperty("dataSource.rewriteBatchedStatements", "true");
		props.setProperty("dataSource.cacheResultSetMetadata", "true");
		props.setProperty("dataSource.cacheServerConfiguration", "true");
		props.setProperty("dataSource.elideSetAutoCommits", "true");
		props.setProperty("dataSource.maintainTimeStats", "false");
		
		HikariConfig hconfig = new HikariConfig(props);
		
		datasource = new HikariDataSource(hconfig);
		datasource.setLeakDetectionThreshold(60 * 1000);
	}

	private void loadDatabases() {
		new PlayerDB();
	}

	public static Connection getConnection() {
		Connection connection = null;

		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			Logger.warning(
					"Something went wrong while getting a connection. Did you create the database specified in config.yml?");
			e.printStackTrace();
		}

		return connection;
	}

	public void close() {
		if (datasource.isClosed() != true) {
			datasource.close();
		}
	}
}
