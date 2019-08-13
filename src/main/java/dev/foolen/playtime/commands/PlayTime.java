package dev.foolen.playtime.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.foolen.playtime.PlayTimePlugin;
import dev.foolen.playtime.databases.PlayerDB;
import dev.foolen.playtime.utils.Logger;
import dev.foolen.playtime.utils.UUIDFetcher;

public class PlayTime implements CommandExecutor {

	private Player targetPlayer;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check if looking up own playtime
		if (args.length == 0) {
			// Check if console isn't looking up its own playtime
			if (!(sender instanceof Player)) {
				Logger.info("A console does not have a playtime!");
				Logger.info("If you want to lookup a player playtime specify a playername.");
				return true;
			}

			// Show own total playtime
			Player p = (Player) sender;
			long totalSecondsPlayed = PlayerDB.getTotalSecondsPlayed(p.getUniqueId());
			
			p.sendMessage(PlayTimePlugin.PREFIX + "You have been online for " + ChatColor.GRAY
					+ getFormattedPlayTime(totalSecondsPlayed) + ChatColor.RESET + ".");
			return true;
		}

		// Check online time from specified player
		String playername = args[0];
		// Check if player is online
		targetPlayer = null;
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (player.getName().equalsIgnoreCase(playername))
				;
			targetPlayer = player;
			return;
		});

		// If not online, find uuid from mojang api
		if (targetPlayer == null) {
			targetPlayer = Bukkit.getPlayer(UUIDFetcher.getUUID(playername));
		}

		long totalSecondsPlayed = PlayerDB.getTotalSecondsPlayed(targetPlayer.getUniqueId());

		// Check if sender is console
		if (!(sender instanceof Player)) {
			Logger.info(playername + " has been online for " + getFormattedPlayTime(totalSecondsPlayed) + ".");
			return true;
		}

		Player p = (Player) sender;
		p.sendMessage(PlayTimePlugin.PREFIX + playername + "has been online for " + ChatColor.GRAY
				+ getFormattedPlayTime(totalSecondsPlayed) + ChatColor.RESET + ".");
		return true;
	}
	
	private String getFormattedPlayTime(long totalSecondsPlayed) {
		final int MINUTES_IN_AN_HOUR = 60;
	    final int SECONDS_IN_A_MINUTE = 60;

	    int seconds = (int) (totalSecondsPlayed % SECONDS_IN_A_MINUTE);
	    int totalMinutes = (int) (totalSecondsPlayed / SECONDS_IN_A_MINUTE);
	    int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
	    int hours = totalMinutes / MINUTES_IN_AN_HOUR;
		
	    return hours + " hours " + minutes + " minutes " + seconds + " seconds";
	}
}
