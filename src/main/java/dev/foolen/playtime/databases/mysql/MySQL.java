package dev.foolen.playtime.databases.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.configuration.Configuration;

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
		datasource = new HikariDataSource();
		String connectionUrl = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;

		datasource.setMaximumPoolSize(10);
		datasource.setJdbcUrl(connectionUrl);
		datasource.setUsername(USERNAME);
		datasource.setPassword(PASSWORD);
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
