package dev.foolen.playtime.databases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import dev.foolen.playtime.databases.mysql.MySQL;

public class PlayerDB {
	public PlayerDB() {
		createTable();
	}

	private void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS `players` (`uuid` varchar(255) NOT NULL,`total_seconds_played` int(11) NOT NULL,PRIMARY KEY(`uuid`))";

		try {
			PreparedStatement pstmt = MySQL.getConnection().prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void savePlayer(UUID uuid, long totalSecondsPlayed) {
		// Check if already in database
		if (isAlreadyInDatabase(uuid)) {
			totalSecondsPlayed += getTotalSecondsPlayed(uuid);

			String sql = "UPDATE `players` SET `total_seconds_played`=? WHERE `uuid`=?";

			try {
				PreparedStatement pstmt = MySQL.getConnection().prepareStatement(sql);

				pstmt.setLong(1, totalSecondsPlayed);
				pstmt.setString(2, uuid.toString());

				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String sql = "INSERT INTO players (`uuid`,`total_seconds_played`) VALUES (?,?)";

			try {
				PreparedStatement pstmt = MySQL.getConnection().prepareStatement(sql);

				pstmt.setString(1, uuid.toString());
				pstmt.setLong(2, totalSecondsPlayed);

				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isAlreadyInDatabase(UUID uuid) {
		boolean isPresent = false;

		String sql = "SELECT * FROM `players` WHERE `uuid`=? LIMIT 1";

		try {
			PreparedStatement pstmt = MySQL.getConnection().prepareStatement(sql);

			pstmt.setString(1, uuid.toString());

			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				isPresent = true;
			}

			result.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isPresent;
	}

	public static long getTotalSecondsPlayed(UUID uuid) {
		long totalSecondsPlayed = 0;

		String sql = "SELECT `total_seconds_played` FROM `players` WHERE `uuid`=? LIMIT 1";

		try {
			PreparedStatement pstmt = MySQL.getConnection().prepareStatement(sql);

			pstmt.setString(1, uuid.toString());

			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				totalSecondsPlayed = Long.parseLong(result.getString("total_seconds_played"));
			}

			result.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return totalSecondsPlayed;
	}
}
